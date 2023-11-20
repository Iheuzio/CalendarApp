package com.example.calendar

import android.content.res.Resources
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EventViewModel() : ViewModel() {
    //private val _uiState = MutableStateFlow(EventUiState())
    //val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    var selectedEvent by mutableStateOf<Event?>(null)
    var selectedDate by mutableStateOf("")
    var events by mutableStateOf(mutableListOf<Event>())
    var idCount by mutableStateOf(0)

    var date by mutableStateOf("")
    var startTime by mutableStateOf("")
    var endTime by mutableStateOf("")
    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var location by mutableStateOf("")

    //FOr when a new event is created
    fun addToList(item: Event) {
        if (item.title.isNotEmpty() && item.title.isNotBlank()) {
            if (!events.contains(item)) {
                val updatedEventItems = events.toMutableList()
                updatedEventItems.add(item)
                events = updatedEventItems
            }
        }
    }

    //For deleting an event
    fun removeFromList(item: Event) {
        val updatedEventItems = events.toMutableList()
        updatedEventItems.remove(item)
        events = updatedEventItems
    }

    fun modifyItem(item: Event, modifiedItem: Event) {
        val updatedEventItems = events.toMutableList()
        var index = events.indexOf(item)
        if (index != -1) {
            updatedEventItems[index] = modifiedItem
            events = updatedEventItems
        }
        else {
            removeFromList(item)
            addToList(modifiedItem)
        }
    }

    //Find an event according to a date
    //replace this by finding with the Event item itself
    fun findItem(id: Int) {
        selectedEvent = events.find { it.id == id } ?: Event()
    }

    fun incrementId() {
        idCount++
    }

}