package com.example.calendar

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.util.*

// Define a simple Event class for the calendar events


class CalendarViewModel : ViewModel() {

    private val _selectedDate = mutableStateOf(Calendar.getInstance().time)
    val selectedDate: MutableState<Date> get() = _selectedDate

    private val _isEventCreationDialogVisible = mutableStateOf(false)
    val isEventCreationDialogVisible: MutableState<Boolean> get() = _isEventCreationDialogVisible

    private val _showDailyOverview = mutableStateOf(false)
    val showDailyOverview: MutableState<Boolean> get() = _showDailyOverview

    private val _events = mutableStateOf(listOf<Event>())
    val events: MutableState<List<Event>> get() = _events

    fun onDateChange(newDate: Calendar) {
        _selectedDate.value = newDate.time
        _showDailyOverview.value = true
    }

    fun onDialogDismiss() {
        _isEventCreationDialogVisible.value = false
    }

    fun onEventSelected(event: Event) {
    }

    fun onAddEvent(event: Event) {
        // Add the event to the list
        _events.value = _events.value + event
    }

    fun toggleShowDailyOverview() {
        _showDailyOverview.value = !_showDailyOverview.value
    }

}
