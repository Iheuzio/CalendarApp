package com.example.calendar.data.database

import androidx.room.*

@Dao
interface EventDao {
    @Query("SELECT * FROM event")
    fun getAll(): List<Event>

    @Query("SELECT * FROM event WHERE id IN (:eventIds)")
    fun loadAllByIds(eventIds: IntArray): List<Event>

    @Query("SELECT * FROM event WHERE date LIKE :date")
    fun findEventsByDate(date: String): List<Event>

    @Insert
    fun insertAll(vararg events: com.example.calendar.data.Event)

    @Delete
    fun delete(event: Event)

    @Update
    fun update(event: com.example.calendar.data.Event)

    @Query("SELECT * FROM event WHERE strftime('%m %Y', date) = :monthYear")
    fun findEventsByMonthAndYear(monthYear: String): List<Event>
}