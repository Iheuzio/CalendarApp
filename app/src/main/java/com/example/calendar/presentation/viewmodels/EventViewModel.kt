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
class EventViewModel(private val database: AppDatabase) : ViewModel() {
    var selectedEvent by mutableStateOf<Event?>(null)
    var events by mutableStateOf(listOf<Event>())
    var isTimeValid by mutableStateOf(true)

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
                        id = item.id,
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

    fun removeFromList(id: Int, database: AppDatabase) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database.eventDao().delete(id)
                events = database.eventDao().getAll().toMutableList()
            }
        }
    }

    private suspend fun getEventsByDate(date: String, database: AppDatabase): List<Event> {
        return withContext(Dispatchers.IO) {
            database.eventDao().findEventsByDate(date)
        }
    }

    fun checkEventIsNotSameTimeSlot(date: String, startTime: String, endTime: String, database: AppDatabase) {
        viewModelScope.launch {
            var isValid = true
            val eventsOnDate = getEventsByDate(date, database)
            for (event in eventsOnDate) {
                if (event.startTime == startTime || event.endTime == endTime ||
                    (convertTimeToInt(event.startTime) >= convertTimeToInt(startTime) && convertTimeToInt(event.startTime) < convertTimeToInt(endTime))) {
                    isValid = false
                }
            }
            isTimeValid = isValid
        }
    }

    private fun convertTimeToInt(timeString: String): Int {
        // Split the time string into hours and minutes
        val (hours, minutes) = timeString.split(":").map { it.toInt() }

        // Convert hours and minutes to a single integer (HH:MM format)
        return hours * 100 + minutes
    }

}