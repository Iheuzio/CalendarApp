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

    var selectedEvent by mutableStateOf(Event())
    var events by mutableStateOf(mutableListOf<Event>())

    //FOr when a new event is created
    fun addToList(item: Event) {
        if (item.title.isNotEmpty() && item.title.isNotBlank()) {
            if (!events.contains(item)) {
                val updatedFoodItems = events.toMutableList()
                updatedFoodItems.add(item)
                events = updatedFoodItems
            }
        }
    }

    //For deleting an event
    fun removeFromList(item: Event) {
        val updatedFoodItems = events.toMutableList()
        events.remove(item)
        events = updatedFoodItems
    }

    //Find an event according to a date
    fun findItem(date: String) {
        selectedEvent = events.find { it.date == date } ?: Event()
    }

}

class Event(
    val date: String = "",
    val time: String = "",
    val title: String = "",
    val description: String = "",
    val location: String = ""
)