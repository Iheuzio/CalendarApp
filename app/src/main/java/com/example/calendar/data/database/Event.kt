package com.example.calendar.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Event(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String,
    var date: String
)