package com.example.calendar

data class Event(
    val id: Int = 0,
    val date: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val title: String = "",
    val description: String = "",
    val location: String = ""
)

