package com.example.calendar.presentation.viewmodels

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
import com.example.calendar.data.database.AppDatabase
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONArray
import org.json.JSONTokener

class HolidayViewModel(private val database: AppDatabase) : ViewModel() {
    var holidays by mutableStateOf(listOf<Holiday>())

    private val countryCode = java.util.Locale.getDefault().country
    private val theUrl = "https://date.nager.at/api/v3/NextPublicHolidays/$countryCode"

    init {
        getData()
    }

    private fun getData() {
        if (database.holidayDao().getAll().isEmpty()) {
            fetchHolidays()
        } else {
            viewModelScope.launch {
                val holidayList = withContext(Dispatchers.IO) {
                    database.holidayDao().getAll().toMutableList()
                }
                holidays = holidayList
            }
        }

    }

    private fun fetchHolidays() {
        viewModelScope.launch {
            val holidayList = fetchData()

            // Add holidays to database
            for (holiday in holidayList) {
                database.holidayDao().insertAll(holiday)
            }
        }
    }

    private suspend fun fetchData(): List<Holiday> = withContext(Dispatchers.IO) {
        val url = URL(theUrl)
        val httpURLConnection = url.openConnection() as HttpURLConnection
        httpURLConnection.requestMethod = "GET"
        httpURLConnection.setRequestProperty("Accept", "text/json")

        //Check if the connection is successful
        val responseCode = httpURLConnection.responseCode

        if (responseCode == HttpURLConnection.HTTP_OK) {
            val dataString = httpURLConnection.inputStream.bufferedReader()
                .use { it.readText() }

            val holidays = mutableListOf<Holiday>()

            val jsonArray = JSONTokener(dataString).nextValue() as JSONArray
            for (i in 0 until jsonArray.length()) {
                // Reformat data
                val dateValues = jsonArray.getJSONObject(i).getString("date").split("-")
                val date = dateValues[1] + "-" + dateValues[2] + "-" + dateValues[0]

                val locations = jsonArray.getJSONObject(i).getString("counties").split(",")

                val holiday = Holiday(
                    date = date,
                    name = jsonArray.getJSONObject(i).getString("name"),
                    description = jsonArray.getJSONObject(i).getString("localName"),
                    location = locations
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
