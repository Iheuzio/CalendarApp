package com.example.calendar.data.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.calendar.data.Event
import java.util.*

class CalendarViewModel : ViewModel() {

    private val _selectedDate = mutableStateOf(Calendar.getInstance().time)
    val selectedDate: MutableState<Date> get() = _selectedDate

    private val _showDailyOverview = mutableStateOf(false)
    val showDailyOverview: MutableState<Boolean> get() = _showDailyOverview

    private val _events = mutableStateOf(listOf<Event>())
    val events: MutableState<List<Event>> get() = _events

    /**
     * Sets the selected date to the given date and toggles the visibility of the event creation dialog.
     * If the given date is the same as the currently selected date, the visibility of the event creation dialog is toggled.
     * Basically you need to press twice to go to the daily overview.
     */
    fun onDateChange(newDate: Calendar) {
        if (_selectedDate.value == newDate.time) {
            _showDailyOverview.value = true
        }
        _selectedDate.value = newDate.time
    }

    /**
     * Toggles the visibility of the event creation dialog.
     */
    fun toggleShowDailyOverview() {
        _showDailyOverview.value = !_showDailyOverview.value
    }

}
