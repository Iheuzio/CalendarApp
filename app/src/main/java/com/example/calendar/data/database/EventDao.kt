package com.example.calendar.data.database

import androidx.room.*

@Dao
interface EventDao {
    /***
     * Returns all events from the database
     * @return List<Event>
     */
    @Query("SELECT * FROM event")
    fun getAll(): List<Event>

    /***
     * Returns all events from the database with the given ids
     * @param eventIds IntArray
     * @return List<Event>
     */
    @Query("SELECT * FROM event WHERE id IN (:eventIds)")
    fun loadAllByIds(eventIds: IntArray): List<Event>

    /***
     * Returns all events from the database with the given date
     * @param date String
     * @return List<Event>
     */
    @Query("SELECT * FROM event WHERE date LIKE :date")
    fun findEventsByDate(date: String): List<Event>

    /***
     * Inserts the given events into the database
     * @param events Array<out Event>
     */
    @Insert
    fun insertAll(vararg events: Event)

    /***
     * Deletes the given event from the database
     * @param event Event
     */
    @Delete
    fun delete(event: Event)

    /***
     * Updates the given event in the database
     * @param event Event
     */
    @Update
    fun update(event: Event)

    /***
     * Returns all events from the database with the given month and year
     * @param monthYear String
     * @return List<Event>
     */
    @Query("SELECT * FROM event WHERE strftime('%m %Y', date) = :monthYear")
    fun findEventsByMonthAndYear(monthYear: String): List<Event>

    /***
     * Returns the event with the given id from the database
     * @param id Int
     * @return Event
     */
    @Query("SELECT * FROM event WHERE id = :id")
    fun getById(id: Int): Event

    /***
     * Updates the event with the given id in the database
     * @param id Int
     * @param title String
     * @param date String
     * @param startTime String
     * @param endTime String
     * @param description String
     * @param location String
     * @param course String
     */
    @Query("UPDATE event SET title = :title, date = :date, startTime = :startTime, endTime = :endTime, description = :description, location = :location, course = :course WHERE id = :id")
    fun updateEvent(id: Int, title: String, date: String, startTime: String, endTime: String, description: String, location: String, course: String)
}