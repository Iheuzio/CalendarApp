package com.example.calendar.data.viewmodels

import android.icu.util.Calendar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.calendar.data.Event
import java.util.Date

class EventViewModel : ViewModel() {
    var selectedEvent by mutableStateOf<Event?>(null)
    var events by mutableStateOf(mutableListOf<Event>())
    var idCount by mutableIntStateOf(0)

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
        val index = events.indexOf(item)
        if (index != -1) {
            updatedEventItems[index] = modifiedItem
            events = updatedEventItems
        } else {
            removeFromList(item)
            addToList(modifiedItem)
        }
    }

    fun getEventById(id: Int): Event? {
        return events.find { it.id == id }
    }

    fun incrementId() {
        idCount++
    }

    private fun getEventsByDate(date: String): List<Event> {
        return events.filter { it.date == date }
    }

    fun checkEventsExist(time: Date): Any {
        val calendar = Calendar.getInstance()
        calendar.time = time
        val date = "${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.DAY_OF_MONTH)}-${calendar.get(Calendar.YEAR)}"
        val events = getEventsByDate(date)
        // return true or false if event is found on that day
        return events.isNotEmpty()
    }

}
