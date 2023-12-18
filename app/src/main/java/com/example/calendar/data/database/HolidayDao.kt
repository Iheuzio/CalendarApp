package com.example.calendar.data.database

import androidx.room.*

@Dao
interface HolidayDao {
    /***
     * Returns all holidays from the database
     * @return List<Holiday>
     */
    @Query("SELECT * FROM holiday")
    fun getAll(): List<Holiday>

    /***
     * Returns all holidays from the database with the given ids
     * @param holidayIds IntArray
     * @return List<Holiday>
     */
    @Query("SELECT * FROM holiday WHERE id IN (:holidayIds)")
    fun loadAllByIds(holidayIds: IntArray): List<Holiday>

    /***
     * Returns all holidays from the database with the given date
     * @param date String
     * @return List<Holiday>
     */
    @Query("SELECT * FROM holiday WHERE date LIKE :date")
    fun findHolidaysByDate(date: String): List<Holiday>

    /***
     * Inserts the given holidays into the database
     * @param holidays Array<out Holiday>
     */
    @Insert
    fun insertAll(vararg holidays: Holiday)

    /***
     * Deletes the given holiday from the database
     * @param id Int
     */
    @Query("DELETE FROM holiday WHERE id = :id")
    fun delete(id: Int)

    /***
     * Updates the given holiday in the database
     * @param holiday Holiday
     */
    @Update
    fun update(holiday: Holiday)

    /***
     * Returns all holidays from the database with the given month and year
     * @param monthYear String
     * @return List<Holiday>
     */
    @Query("SELECT * FROM holiday WHERE strftime('%m %Y', date) = :monthYear")
    fun findHolidaysByMonthAndYear(monthYear: String): List<Holiday>

    /***
     * Returns the holiday with the given id from the database
     * @param id Int
     * @return Holiday
     */
    @Query("SELECT * FROM holiday WHERE id = :id")
    fun findHolidayById(id: Int): Holiday
}