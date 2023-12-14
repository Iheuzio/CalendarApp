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
    var holidays by mutableStateOf(listOf<Holiday>())

    init {
        getData()
    }

    fun getData() {
        //Create a coroutine to fetch the data
        viewModelScope.launch(Dispatchers.IO) {
            val fetchedHolidays = GetHolidayData(utilityHelper).fetchData()

            // Update Compose state on the main thread
            launch(Dispatchers.Main) {
                holidays = fetchedHolidays
            }
        }
    }

}

data class Holiday(
    var date: String,
    var name: String,
    var description: String,
    var location: List<String>,
)
