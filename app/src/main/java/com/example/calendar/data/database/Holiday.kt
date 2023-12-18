package com.example.calendar.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class for the holiday entity
 * @param id Int
 * @param name String
 * @param date String
 * @param description String
 * @param location List<String>
 */
@Entity
data class Holiday(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String,
    var date: String,
    var description: String,
    var location: String
)