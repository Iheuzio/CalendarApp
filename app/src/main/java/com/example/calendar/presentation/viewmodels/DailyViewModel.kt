package com.example.calendar.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calendar.data.Event

class DailyViewModel : ViewModel() {
    val eventsForSelectedDate = MutableLiveData<List<Event>>()
}