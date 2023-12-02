package com.example.calendar.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class for the event entity
 * @param id Int
 * @param title String
 * @param date String
 * @param startTime String
 * @param endTime String
 * @param description String
 * @param location String
 * @param course String
 */
@Entity
data class Event(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String,
    var date: String,
    var startTime: String,
    var endTime: String,
    var description: String,
    var location: String,
    var course: String
)