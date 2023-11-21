package com.example.calendar.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calendar.data.Event
import java.util.Date

class DailyViewModel : ViewModel() {

    val eventsForSelectedDate = MutableLiveData<List<Event>>()

    fun loadEventsForDate(date: Date) {
    }


}