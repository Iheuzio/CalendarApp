package com.example.calendar.calendarView

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.testing.TestNavHostController
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.calendar.data.database.AppDatabase
import com.example.calendar.presentation.viewmodels.CalendarViewModel
import com.example.calendar.presentation.viewmodels.EventViewModel
import com.example.calendar.presentation.screen.CalendarHeader
import com.example.calendar.presentation.screen.DayNameText
import com.example.calendar.presentation.screen.DayOfWeekHeader
import com.example.calendar.presentation.screen.monthYearHeader
import kotlinx.coroutines.ExperimentalCoroutinesApi
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class CalendarHeaderTest {

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

    }

    @Test
    fun calendarHeader_changesMonth_whenNextMonthButtonClicked() {
        val initialMonth = Calendar.getInstance().get(Calendar.MONTH)
        composeTestRule.setContent {
            CalendarHeader(calendarModel.selectedDate.value) { newDate ->
                calendarModel.onDateChange(newDate)
            }
        }
        composeTestRule.onNodeWithContentDescription("Next Month").performClick()
        assertNotEquals(initialMonth, calendarModel.selectedDate.value.month)
    }
    @Test
    fun monthYearHeader_displaysCorrectMonthAndYear() {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2022)
            set(Calendar.MONTH, Calendar.JANUARY)
        }

        composeTestRule.setContent {
            monthYearHeader(calendar)
        }

        composeTestRule.onNodeWithText("January 2022").assertExists()
    }

    @Test
    fun dayOfWeekHeader_displaysAllDaysOfWeek() {
        composeTestRule.setContent {
            DayOfWeekHeader()
        }

        listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { dayName ->
            composeTestRule.onNodeWithText(dayName).assertExists()
        }
    }

    @Test
    fun dayNameText_displaysCorrectDayName() {
        val dayName = "Monday"

        composeTestRule.setContent {
            DayNameText(dayName)
        }

        composeTestRule.onNodeWithText(dayName).assertExists()
    }
}
