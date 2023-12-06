package com.example.calendar.utils

import android.content.Context
import android.util.Log

import java.io.IOException

class TempStorage(utilityHelper: UtilityHelper) {
    private val utilHelper = utilityHelper

    fun writeDataToFile(dataString: String, filename: String) {
        try {
            utilHelper.appContext.openFileOutput(filename, Context.MODE_PRIVATE).use {
                it.write(dataString.toByteArray())
            }
        } catch (e: IOException) {
            Log.d("fetchData", "Error writing data to file: ${e.message}")
        }
    }

    fun readDataFromFile(filename: String): String {
        //String to store the data
        var theData = ""

        try {
            theData = utilHelper.appContext.openFileInput(filename).bufferedReader()
                .use { it.readText() }

        } catch (e: IOException) {
            Log.e("readDataFromFile", "ERROR reading data from file")
        }
        return theData
    }
}
