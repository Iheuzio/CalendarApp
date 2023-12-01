package com.example.calendar.presentation.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendar.data.database.AppDatabase
import com.example.calendar.data.database.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class CalendarViewModel(private val database: AppDatabase) : ViewModel() {

    private val _selectedDate = mutableStateOf(Calendar.getInstance().time)
    val selectedDate: MutableState<Date> get() = _selectedDate

    private val _showDailyOverview = mutableStateOf(false)
    val showDailyOverview: MutableState<Boolean> get() = _showDailyOverview

    private val _events = mutableStateOf(listOf<Event>())
    val events: MutableState<List<Event>> get() = _events

    init {
        fetchEvents()
    }

    private fun fetchEvents() {
        viewModelScope.launch {
            val eventsList = withContext(Dispatchers.IO) {
                database.eventDao().getAll()
            }
            _events.value = eventsList
        }
    }

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
        fetchEventsForDate(newDate)
    }

    private fun fetchEventsForDate(date: Calendar) {
        val dateString = "${date.get(Calendar.MONTH) + 1}-${date.get(Calendar.DAY_OF_MONTH)}-${date.get(Calendar.YEAR)}"
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                events.value = database.eventDao().findEventsByDate(dateString)
            }
        }
    }

    /**
     * Toggles the visibility of the event creation dialog.
     */
    fun toggleShowDailyOverview() {
        _showDailyOverview.value = !_showDailyOverview.value
    }

}