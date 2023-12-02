package com.example.calendar.DailyView
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.testing.TestNavHostController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.calendar.data.Event
import com.example.calendar.data.NavRoutes
import com.example.calendar.data.database.AppDatabase
import com.example.calendar.presentation.viewmodels.CalendarViewModel
import com.example.calendar.presentation.viewmodels.EventViewModel
import com.example.calendar.presentation.screen.CalendarGrid
import com.example.calendar.presentation.screen.DailyOverview
import com.example.calendar.presentation.viewmodels.DailyViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalCoroutinesApi
class DailyOverviewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController
    private lateinit var dailyViewModel: DailyViewModel
    private lateinit var calendarViewModel: CalendarViewModel
    private lateinit var eventViewModel: EventViewModel

    @Before
    fun setup() {
        navController = TestNavHostController(InstrumentationRegistry.getInstrumentation().targetContext)
        dailyViewModel = DailyViewModel(database = AppDatabase.getInstance())
        calendarViewModel = CalendarViewModel()
        eventViewModel = EventViewModel()
        composeTestRule.runOnUiThread {
            dailyViewModel.eventsForSelectedDate.value = createMockEvents()
            composeTestRule.setContent {
                DailyOverview(navController, calendarViewModel, dailyViewModel, eventViewModel)
            }
        }
    }

    @Test
    fun dailyOverview_displaysCorrectDate() {
        val expectedDateFormat = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault())
        val expectedDate = expectedDateFormat.format(calendarViewModel.selectedDate.value)
        composeTestRule.onNodeWithText(expectedDate).assertIsDisplayed()
    }
    @Test
    fun dailyOverview_changesDateOnNavigation_Next() {
        val initialDate = Date()
        calendarViewModel.selectedDate.value = initialDate

        val contentDescription = "Next"

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithContentDescription(contentDescription).performClick()

        composeTestRule.waitForIdle()

        assertNotEquals(initialDate, calendarViewModel.selectedDate.value)
    }
    @Test
    fun dailyOverview_changesDateOnNavigation_Previous() {
        val initialDate = Date()
        calendarViewModel.selectedDate.value = initialDate

        val contentDescription = "Back"

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithContentDescription(contentDescription).performClick()

        composeTestRule.waitForIdle()

        assertNotEquals(initialDate, calendarViewModel.selectedDate.value)
    }
    @Test
    fun dailyOverview_displaysEventsCorrectly() {
        val initialDate = Date()
        calendarViewModel.selectedDate.value = initialDate
        val mockEvents = createMockEvents()

        composeTestRule.runOnUiThread {
            dailyViewModel.eventsForSelectedDate.value = mockEvents
        }
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Event").assertDoesNotExist()

    }
    private fun createMockEvents(): List<Event> {
        return listOf(
            Event(id = 1, date = "2023-11-20", startTime = "1:00", endTime = "11:00", title = "Event", description = "Description 1", location = "Location 1"),
            Event(id = 2, date = "2023-11-20", startTime = "12:00", endTime = "13:00", title = "Event 2", description = "Description 2", location = "Location 2")
        )
    }
}
