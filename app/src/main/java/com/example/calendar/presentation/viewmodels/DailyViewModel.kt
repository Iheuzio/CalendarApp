package com.example.calendar.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendar.data.database.AppDatabase
import com.example.calendar.data.database.Event
import kotlinx.coroutines.launch

class DailyViewModel(private val database: AppDatabase) : ViewModel() {
    val eventsForSelectedDate = MutableLiveData<List<Event>>()

    fun fetchEventsForDate(date: String) {
        viewModelScope.launch {
            eventsForSelectedDate.value = database.eventDao().findEventsByDate(date)
        }
    }
}