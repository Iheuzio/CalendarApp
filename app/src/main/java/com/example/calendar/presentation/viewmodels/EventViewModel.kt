package com.example.calendar.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendar.data.database.AppDatabase
import com.example.calendar.data.database.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class EventViewModel(private val database: AppDatabase) : ViewModel() {
    var selectedEvent by mutableStateOf<Event?>(null)
    var events by mutableStateOf(listOf<Event>())

    init {
        fetchEvents()
    }

    private fun fetchEvents() {
        viewModelScope.launch {
            val eventsList = withContext(Dispatchers.IO) {
                database.eventDao().getAll().toMutableList()
            }
            events = eventsList
        }
    }

    fun addToList(item: Event, database: AppDatabase) {
        if (item.title.isNotEmpty() && item.title.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                database.eventDao().insertAll(
                    Event(
                        title = item.title,
                        date = item.date,
                        startTime = item.startTime,
                        endTime = item.endTime,
                        description = item.description,
                        location = item.location,
                        course = item.course
                    )
                )
                val updatedEvents = database.eventDao().getAll().toMutableList()
                withContext(Dispatchers.Main) {
                    events = updatedEvents
                }
            }
        }
    }

    fun modifyItem(item: Event, modifiedItem: Event, database: AppDatabase) {
        viewModelScope.launch {
            withContext(Dispatchers.IO)
            {
                database.eventDao().update(
                    Event(
                        id = item.id,
                        title = modifiedItem.title,
                        date = modifiedItem.date,
                        startTime = modifiedItem.startTime,
                        endTime = modifiedItem.endTime,
                        description = modifiedItem.description,
                        location = modifiedItem.location,
                        course = modifiedItem.course
                    )
                )
                events = database.eventDao().getAll().toMutableList()
            }
        }
    }

    fun removeFromList(item: Event, database: AppDatabase) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database.eventDao().delete(item)
                events = database.eventDao().getAll().toMutableList()
            }
        }
    }

    fun getEventById(id: Int): Event? {
        return events.find { it.id == id }
    }

    private fun getEventsByDate(date: String, database: AppDatabase): List<Event> {
        var filteredEvents: List<Event> = listOf()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                filteredEvents = database.eventDao().findEventsByDate(date)
            }
        }
        return filteredEvents
    }

    fun checkEventsExist(time: Date): Any {
        val dateString = "${time.month + 1}-${time.date}-${time.year}"
        return getEventsByDate(dateString, database)
    }
}