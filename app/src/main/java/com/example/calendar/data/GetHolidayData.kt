package com.example.calendar.data

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import com.example.downloadandsavetostorage.presentation.viewmodel.Holiday
import com.example.downloadandsavetostorage.presentation.viewmodel.HolidayViewModel
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONArray
import org.json.JSONTokener

class GetHolidayData(utilityHelper: UtilityHelper) {
    private val theUrl = "https://date.nager.at/api/v3/NextPublicHolidays/CA"
    private val utilHelper = utilityHelper

    fun fetchData(tempFile: String) {
        val url = URL(theUrl)
        val httpURLConnection = url.openConnection() as HttpURLConnection
        httpURLConnection.requestMethod = "GET"
        httpURLConnection.setRequestProperty("Accept", "text/json")

        //Check if the connection is successful
        val responseCode = httpURLConnection.responseCode

        if (responseCode == HttpURLConnection.HTTP_OK) {
            val dataString = httpURLConnection.inputStream.bufferedReader()
                .use { it.readText() }

            val holidays by mutableStateOf(mutableListOf<Holiday>())

            val jsonArray = JSONTokener(dataString).nextValue() as JSONArray
            for (i in 0 until jsonArray.length()) {
                // ID
                val holiday = Holiday(jsonArray.getJSONObject(i).getString("date"),
                    jsonArray.getJSONObject(i).getString("localName"),
                    jsonArray.getJSONObject(i).getString("types"),
                    jsonArray.getJSONObject(i).getString("counties")
                )

                holidays += holiday

            }
            print("Hello")

            //Write the data string to the temp file
            //TempStorage(utilHelper).writeDataToFile(dataString, tempFile)

        } else {
            Log.e("httpsURLConnection_ERROR", responseCode.toString())
        }
    }
}


