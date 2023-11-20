package com.example.calendar.calendarView

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.testing.TestNavHostController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.calendar.data.NavRoutes
import com.example.calendar.presentation.CalendarView
import com.example.calendar.data.viewmodels.CalendarViewModel
import com.example.calendar.data.viewmodels.EventViewModel
import com.example.calendar.presentation.CalendarGrid
import com.example.calendar.presentation.CalendarHeader
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
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
        calendarModel = CalendarViewModel()
        eventViewModel = EventViewModel()
        calendarModel.showDailyOverview.value = false
        composeTestRule.setContent {
            CalendarGrid(calendarModel.selectedDate.value) { day ->
                calendarModel.onDateChange(day)
            }
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
