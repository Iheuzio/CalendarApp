package com.example.calendar

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

    fun incrementId() {
        idCount++
    }

}