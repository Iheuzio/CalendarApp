package com.example.calendar.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendar.data.database.AppDatabase
import com.example.calendar.data.database.Event
import kotlinx.coroutines.launch
import java.util.Date

class EventViewModel(private val database: AppDatabase) : ViewModel() {
    var selectedEvent by mutableStateOf<Event?>(null)
    var events by mutableStateOf(mutableListOf<Event>())

    init {
        fetchEvents()
    }

    private fun fetchEvents() {
        viewModelScope.launch {
            events = database.eventDao().getAll().toMutableList()
        }
    }

    fun addToList(item: com.example.calendar.data.Event, database: AppDatabase) {
        if (item.title.isNotEmpty() && item.title.isNotBlank()) {
            viewModelScope.launch {
                database.eventDao().insertAll(item)
                events = database.eventDao().getAll().toMutableList()
            }
        }
    }

    fun modifyItem(item: Event, modifiedItem: com.example.calendar.data.Event, database: AppDatabase) {
        viewModelScope.launch {
            database.eventDao().update(modifiedItem)
            events = database.eventDao().getAll().toMutableList()
        }
    }

    fun removeFromList(item: Event, database: AppDatabase) {
        viewModelScope.launch {
            database.eventDao().delete(item)
            events = database.eventDao().getAll().toMutableList()
        }
    }

    fun getEventById(id: Int): Event? {
        return events.find { it.id == id }
    }

    private fun getEventsByDate(date: String): List<Event> {
        return events.filter { it.date == date }
    }

    fun checkEventsExist(time: Date): Any {
        val dateString = "${time.month + 1}-${time.date}-${time.year}"
        return getEventsByDate(dateString)
    }
}