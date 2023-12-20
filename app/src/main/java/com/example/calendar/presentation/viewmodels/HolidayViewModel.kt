package com.example.calendar.presentation.viewmodels

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.calendar.data.database.Holiday
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log
import androidx.core.content.ContextCompat
import android.Manifest
import com.example.calendar.data.database.AppDatabase
import com.google.android.gms.location.FusedLocationProviderClient
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONArray
import org.json.JSONTokener
import com.google.android.gms.location.LocationServices
import java.util.Locale

class HolidayViewModel(private val database: AppDatabase, private val context: Context) : ViewModel() {
    var holidays by mutableStateOf(listOf<Holiday>())

    private var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private var countryCode: String = Locale.getDefault().country

    private var theUrl = "https://date.nager.at/api/v3/NextPublicHolidays/$countryCode"

    init {
        viewModelScope.launch {
            val permission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            if (permission == PackageManager.PERMISSION_GRANTED) {
                val task = fusedLocationClient.lastLocation
                task.addOnSuccessListener { location ->
                    if (location != null) {
                        val geocoder = Geocoder(context)
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        countryCode = addresses?.get(0)?.countryCode ?: "CA"
                        theUrl = "https://date.nager.at/api/v3/NextPublicHolidays/$countryCode"
                        getData()
                    }
                }
            } else {
                getData()
            }
        }
    }


    private fun getData() {
        viewModelScope.launch {
            val holidayList = withContext(Dispatchers.IO) {
                database.holidayDao().getAll().toMutableList()
            }
            holidays = holidayList

            if (holidays.isEmpty()) {
                fetchHolidays()
            }
        }
    }

     fun fetchHolidays(location: String? = null) {
         //For testing purpose
         if (location != null) {
             countryCode = location
         }
        viewModelScope.launch {
            val holidayList = fetchData()

            // Add holidays to database
            withContext(Dispatchers.IO) {
                for (holiday in holidayList) {
                    database.holidayDao().insertAll(holiday)
                }
                holidays = holidayList
            }
        }
    }

    suspend fun fetchData(location: String? = null): List<Holiday> = withContext(Dispatchers.IO) {
        //For testing purpose
        if (location != null) {
            countryCode = location
        }
        val url = URL(theUrl)
        val httpURLConnection = url.openConnection() as HttpURLConnection
        httpURLConnection.requestMethod = "GET"
        httpURLConnection.setRequestProperty("Accept", "text/json")

        //Check if the connection is successful
        val responseCode = httpURLConnection.responseCode

        if (responseCode == HttpURLConnection.HTTP_OK) {
            val dataString = httpURLConnection.inputStream.bufferedReader()
                .use { it.readText() }

            val jsonArray = JSONTokener(dataString).nextValue() as JSONArray
            for (i in 0 until jsonArray.length()) {
                // Reformat data
                val dateValues = jsonArray.getJSONObject(i).getString("date").split("-")
                val date = dateValues[1] + "-" + dateValues[2] + "-" + dateValues[0]

                val locations = jsonArray.getJSONObject(i).getString("counties").split(",")

                // convert locations to a string delimited by commas
                var locationsString = ""
                for (location in locations) {
                    locationsString += "$location, "
                }

                val holiday = Holiday(
                    date = date,
                    name = jsonArray.getJSONObject(i).getString("name"),
                    description = jsonArray.getJSONObject(i).getString("localName"),
                    location = countryCode
                )

                holidays += holiday

            }

            return@withContext holidays

        } else {
            Log.e("httpsURLConnection_ERROR", responseCode.toString())
            return@withContext emptyList()
        }
    }
}
