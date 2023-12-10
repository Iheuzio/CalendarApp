package com.example.calendar.data

import android.util.Log
import java.net.HttpURLConnection
import java.net.URL

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

            //Write the data string to the temp file
            TempStorage(utilHelper).writeDataToFile(dataString, tempFile)

        } else {
            Log.e("httpsURLConnection_ERROR", responseCode.toString())
        }
    }
}


