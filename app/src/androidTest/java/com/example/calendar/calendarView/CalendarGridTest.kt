package com.example.calendar.calendarView

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.testing.TestNavHostController
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.example.calendar.data.database.AppDatabase
import com.example.calendar.presentation.viewmodels.CalendarViewModel
import com.example.calendar.presentation.viewmodels.EventViewModel
import com.example.calendar.presentation.screen.CalendarGrid
import com.example.calendar.presentation.viewmodels.HolidayViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class CalendarGridTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController
    private lateinit var calendarModel: CalendarViewModel
    private lateinit var eventViewModel : EventViewModel

    @Before
    fun setup() {
        navController =
            TestNavHostController(InstrumentationRegistry.getInstrumentation().targetContext)
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        calendarModel = CalendarViewModel(db)
        eventViewModel = EventViewModel(db)
        val holidayModel = HolidayViewModel(db, context)
        calendarModel.showDailyOverview.value = false
        composeTestRule.setContent {
            CalendarGrid(eventViewModel,calendarModel.selectedDate.value, { day ->
                calendarModel.onDateChange(day)
            }, holidayModel)
        }
    }

    @Test
    fun calendarGrid_checkIfDaySelectedInit() {
        // get current day and check if it is selected
        val initialDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        assertEquals(initialDay, calendarModel.selectedDate.value.date)
    }

    @Test
    fun calendarGrid_checkIfDayChanged() {
        calendarModel.selectedDate.value = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 2)
        }.time
        assertEquals(2, calendarModel.selectedDate.value.date)
    }
}
