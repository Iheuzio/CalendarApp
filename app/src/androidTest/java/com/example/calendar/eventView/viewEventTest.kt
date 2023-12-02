package com.example.calendar.eventView

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import androidx.navigation.testing.TestNavHostController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.calendar.presentation.screen.ViewEventScreen
import com.example.calendar.presentation.viewmodels.EventViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ViewEventTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController
    private lateinit var eventViewModel : EventViewModel

    @Before
    fun setup() {
        navController =
            TestNavHostController(InstrumentationRegistry.getInstrumentation().targetContext)
        eventViewModel = EventViewModel()
    }

    @Test
    fun viewEventScreen_checkEventDetailsDisplayed() {
        // Create a sample event for testing
        val sampleEvent = Event(
            id = 1,
            date = "11-28-2023",
            startTime = "12:00",
            endTime = "14:00",
            title = "Sample Event",
            description = "This is a sample event",
            location = "Sample Location",
            course = "Sample Course"
        )

        // Set up the composable with the sample event
        composeTestRule.setContent {
            val navController = rememberNavController()
            val viewModel = EventViewModel()
            viewModel.selectedEvent = sampleEvent
            ViewEventScreen(viewModel, navController)
        }

        // Verify that the event details are displayed
        composeTestRule
            .onNodeWithText("Title: Sample Event")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Select date: 11-28-2023")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Select start time: 12:00")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Select end time: 14:00")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Description: This is a sample event")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Location: Sample Location")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Course: Sample Course")
            .assertIsDisplayed()
    }

    @Test
    fun viewEventScreen_checkDeleteButton() {
        // Set up the composable with a selected event
        composeTestRule.setContent {
            eventViewModel.selectedEvent = Event(1, "11-28-2023", "12:00", "14:00")
            ViewEventScreen(eventViewModel, navController)
        }

        // Verify that the Delete button is displayed
        composeTestRule
            .onNodeWithText("Delete")
            .assertIsDisplayed()
    }

    @Test
    fun viewEventScreen_checkBackButton() {
        // Set up the composable with a selected event
        composeTestRule.setContent {
            eventViewModel.selectedEvent = Event(1, "11-28-2023", "12:00", "14:00")
            ViewEventScreen(eventViewModel, navController)
        }

        // Verify that the Delete button is displayed
        composeTestRule
            .onNodeWithText("Done")
            .assertIsDisplayed()
    }
}