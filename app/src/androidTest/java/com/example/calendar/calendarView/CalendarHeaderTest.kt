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
        calendarModel = CalendarViewModel()
        eventViewModel = EventViewModel()
        composeTestRule.setContent {
            CalendarHeader(calendarModel.selectedDate.value) { newDate ->
                calendarModel.onDateChange(newDate)
            }
        }
    }

    @Test
    fun calendarHeader_changesMonth_whenNextMonthButtonClicked() {
        val initialMonth = Calendar.getInstance().get(Calendar.MONTH)
        composeTestRule.onNodeWithContentDescription("Next Month").performClick()
        assertNotEquals(initialMonth, calendarModel.selectedDate.value.month)
    }
}
