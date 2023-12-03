package com.example.calendar.eventView

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import androidx.navigation.testing.TestNavHostController
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.example.calendar.data.database.AppDatabase
import com.example.calendar.data.database.Event
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
    private lateinit var db : AppDatabase

    @Before
    fun setup() {
        navController =
            TestNavHostController(InstrumentationRegistry.getInstrumentation().targetContext)
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        eventViewModel = EventViewModel(db)
    }

    @Test
    fun viewEventScreen_checkEventDetailsDisplayed() {
        // Create a sample event for testing
        val sampleEvent = Event(
            id = 1,
            title = "Sample Event",
            date = "11-28-2023",
            startTime = "12:00",
            endTime = "14:00",
            description = "This is a sample event",
            location = "Sample Location",
            course = "Sample Course"
        )

        // Set up the composable with the sample event
        composeTestRule.setContent {
            val navController = rememberNavController()
            val viewModel = EventViewModel(db)
            viewModel.selectedEvent = sampleEvent
            ViewEventScreen(viewModel, navController, db)
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
            eventViewModel.selectedEvent = Event(1,
                "11-28-2023",
                "title",
                "12:00",
                "14:00",
                "description",
                "location",
                "course")
            ViewEventScreen(eventViewModel, navController, db)
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
            eventViewModel.selectedEvent = Event(1,
                "title",
                "11-28-2023",
                "12:00",
                "14:00",
                "description",
                "location",
                "course")
            ViewEventScreen(eventViewModel, navController, db)
        }

        // Verify that the Delete button is displayed
        composeTestRule
            .onNodeWithText("Done")
            .assertIsDisplayed()
    }
}