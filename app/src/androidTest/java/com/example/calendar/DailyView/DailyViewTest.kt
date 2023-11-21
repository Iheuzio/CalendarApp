package com.example.calendar.DailyView
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.testing.TestNavHostController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.calendar.data.Event
import com.example.calendar.presentation.viewmodels.CalendarViewModel
import com.example.calendar.presentation.viewmodels.EventViewModel
import com.example.calendar.presentation.screen.CalendarGrid
import com.example.calendar.presentation.screen.DailyOverview
import com.example.calendar.presentation.screen.DailyViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
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
        dailyViewModel = DailyViewModel()
        calendarViewModel = CalendarViewModel()
        eventViewModel = EventViewModel()

        dailyViewModel.eventsForSelectedDate.value = createMockEvents() // Set mock events here

        composeTestRule.setContent {
            DailyOverview(navController, calendarViewModel, dailyViewModel, eventViewModel )
        }
    }

    @Test
    fun dailyOverview_displaysCorrectDate() {
        // Verify that the header displays the correct date
        val expectedDateFormat = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault())
        val expectedDate = expectedDateFormat.format(calendarViewModel.selectedDate.value)

    }

    @Test
    fun dailyOverview_navigatesToEventDetails_whenEventClicked() {
        val firstEvent = createMockEvents().first()
        composeTestRule.onNodeWithText(firstEvent.title).performClick()
        assertEquals(navController.currentDestination?.route, "event-view/${firstEvent.id}")
    }



    @Test
    fun dailyOverview_showsNoEventsMessage_whenNoEvents() {
        dailyViewModel.eventsForSelectedDate.value = emptyList()
        composeTestRule.onNodeWithText("No Events").assertIsDisplayed()
    }
    @Test
    fun dailyOverview_displaysEventsForSelectedDate() {
        val mockEvents = createMockEvents()
        dailyViewModel.eventsForSelectedDate.value = mockEvents
        mockEvents.forEach { event ->
            composeTestRule.onNodeWithText(event.title).assertIsDisplayed()
            composeTestRule.onNodeWithText(event.description).assertIsDisplayed()
            // Add more assertions as needed
        }
    }
    private fun createMockEvents(): List<Event> {
        return listOf(
            Event(id = 1, date = "2023-11-20", startTime = "10:00", endTime = "11:00", title = "Event 1", description = "Description 1", location = "Location 1"),
            Event(id = 2, date = "2023-11-20", startTime = "12:00", endTime = "13:00", title = "Event 2", description = "Description 2", location = "Location 2")
            // Add more mock events as needed
        )
    }

}
