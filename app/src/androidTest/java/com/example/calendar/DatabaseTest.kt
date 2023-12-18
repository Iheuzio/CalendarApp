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
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private lateinit var eventDao: EventDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        eventDao = db.eventDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun testEventCreationInDB() {
        val event = Event(0,
            "test title",
            "12-02-2023",
            "12:00",
            "1:00",
            "test description",
            "test location",
            "test course")
        eventDao.insertAll(event)
        val returnedEvents = eventDao.findEventsByDate("12-02-2023")

        assertEquals(1, returnedEvents.size)
        assertEquals(event.title, returnedEvents[0].title)
    }

    @Test
    fun testEventEditingInDB() {
        val event = Event(0,
            "test title",
            "12-02-2023",
            "12:00",
            "1:00",
            "test description",
            "test location",
            "test course")
        eventDao.insertAll(event)
        eventDao.updateEvent(1,
            "better test title",
            "12-02-2023",
            "12:00",
            "1:00",
            "test description",
            "test location",
            "test course")
        val editedEvent = eventDao.findEventsByDate("12-02-2023")
        assertEquals("better test title", editedEvent[0].title)
    }

    @Test
    fun testDeleteEventInDB() {
        val event = Event(0,
            "test title",
            "12-02-2023",
            "12:00",
            "1:00",
            "test description",
            "test location",
            "test course")
        eventDao.insertAll(event)
        eventDao.delete(1)
        val existingEvents = eventDao.findEventsByDate("12-02-2023")
        //assertEquals(0, existingEvents.size)
    }

    @Test
    fun testGetAllEventsInDB() {
        val event1 = Event(0,
            "test title 1",
            "12-02-2023",
            "12:00",
            "1:00",
            "test description",
            "test location",
            "test course")
        eventDao.insertAll(event1)
        val event2 = Event(0,
            "test title 2",
            "12-02-2023",
            "12:00",
            "1:00",
            "test description",
            "test location",
            "test course")
        eventDao.insertAll(event2)
        val events = eventDao.getAll()
        assertEquals(2, events.size)
        assertEquals("test title 2", events[1].title)
    }
}