package com.example.downloadandsavetostorage.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.calendar.data.GetHolidayData
import com.example.calendar.data.TempStorage
import com.example.calendar.data.UtilityHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HolidayViewModel(private val utilityHelper: UtilityHelper): ViewModel() {
    //Stores the data that will be read from the temp file
    var theDataState = mutableStateOf("")
    private val filename = "holidayData"

    fun getdata() {
        //Create a coroutine to fetch the data
        viewModelScope.launch(Dispatchers.IO) {
            GetHolidayData(utilityHelper).fetchData(filename)
        }
    }

    fun getDataFromFile() {
        /*Fetch the data asynchronously via a coroutine. It needs to run on the
        Main thread, since the data is required by the Main thread.
         */
        viewModelScope.launch(Dispatchers.Main) {
            val result = TempStorage(utilityHelper).readDataFromFile(filename)
            theDataState.value = result
        }
    }
}
