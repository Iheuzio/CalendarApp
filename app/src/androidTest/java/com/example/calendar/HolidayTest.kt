package com.example.calendar

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.calendar.data.database.AppDatabase
import com.example.calendar.data.database.Event
import com.example.calendar.data.database.EventDao
import com.example.calendar.data.database.Holiday
import com.example.calendar.data.database.HolidayDao
import com.example.calendar.presentation.viewmodels.EventViewModel
import com.example.calendar.presentation.viewmodels.HolidayViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class HolidayTest {

    private lateinit var holidayDao: HolidayDao
    private lateinit var db: AppDatabase
    private lateinit var holidayModel: HolidayViewModel

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        holidayDao = db.holidayDao()
        holidayModel = HolidayViewModel(db, context)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    @Throws(Exception::class)
    fun testFetchHolidays() = runTest {

        var holidays: List<Holiday> = emptyList()
        // Launch the coroutine and wait for it to complete
        launch { holidays = holidayModel.fetchData("CA") }.join()

        // Ensure that asynchronous operations are completed
        advanceUntilIdle()

        assertFalse(holidays.isEmpty())
    }

    @Test
    fun addHolidayToDb() {
        val holiday = Holiday(0, "test holiday", "12-02-2023", "test description", "test location")
        holidayDao.insertAll(holiday)
        val dbHolidays = holidayDao.getAll()
        assertEquals(1, dbHolidays.size)
        assertEquals(holiday.name, dbHolidays[0].name)
    }
}