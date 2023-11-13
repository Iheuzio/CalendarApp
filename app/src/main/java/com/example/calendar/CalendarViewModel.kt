package com.example.calendar

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.util.*

class CalendarViewModel : ViewModel() {
    private val _selectedDate = mutableStateOf(Calendar.getInstance().time)
    val selectedDate: MutableState<Date> get() = _selectedDate

    private val _isEventCreationDialogVisible = mutableStateOf(false)
    val isEventCreationDialogVisible: MutableState<Boolean> get() = _isEventCreationDialogVisible

    fun onDateChange(newDate: Calendar) {
        _selectedDate.value = newDate.time
    }

    fun toggleEventCreationDialogVisibility() {
        _isEventCreationDialogVisible.value = !_isEventCreationDialogVisible.value
    }
}
