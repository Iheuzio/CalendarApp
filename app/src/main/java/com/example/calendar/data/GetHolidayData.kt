package com.example.calendar.data

import android.util.Log
import com.example.calendar.presentation.viewmodels.Holiday
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONArray
import org.json.JSONTokener

class GetHolidayData {
    private val theUrl = "https://date.nager.at/api/v3/NextPublicHolidays/CA"

    fun fetchData(): List<Holiday> {
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

                val holiday = Holiday(date,
                    jsonArray.getJSONObject(i).getString("localName"),
                    jsonArray.getJSONObject(i).getString("types"),
                    locations
                )

                holidays += holiday

            }

            return holidays

        } else {
            Log.e("httpsURLConnection_ERROR", responseCode.toString())
            return emptyList()
        }
    }
}


