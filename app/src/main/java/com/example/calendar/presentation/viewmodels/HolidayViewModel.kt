package com.example.calendar.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    var holidays by mutableStateOf(listOf<Holiday>())

    init {
        getData()
    }

    fun getData() {
        //Create a coroutine to fetch the data
        viewModelScope.launch(Dispatchers.IO) {
            holidays = GetHolidayData(utilityHelper).fetchData()
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

data class Holiday(
    var date: String,
    var name: String,
    var description: String,
    var location: String,
)
