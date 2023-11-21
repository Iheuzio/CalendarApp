package com.example.calendar.eventView

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.testing.TestNavHostController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.calendar.data.Event
import com.example.calendar.presentation.screen.CreateEditEventScreen
import com.example.calendar.presentation.viewmodels.EventViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Calendar

@ExperimentalCoroutinesApi
class CreateEditEventTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController
    private lateinit var eventViewModel : EventViewModel

    @Before
    fun setup() {
        navController =
            TestNavHostController(InstrumentationRegistry.getInstrumentation().targetContext)
        eventViewModel = EventViewModel()
        composeTestRule.setContent {
            CreateEditEventScreen(viewModel = eventViewModel,
                navController = navController,
                inputDate = "11-28-2023",
                inputEvent = Event(0, "11-28-2023", "12:00", "12:00", "Title")
            )
        }
    }

    @Test
    fun createEditEventScreen_checkIfTitleIsDisplayed() {
        // Verify that the "Title" text field is displayed
        composeTestRule.onNodeWithText("Title").assertIsDisplayed()
    }

    @Test
    fun createEditEventScreen_checkIfDateIsDisplayed() {

        // Verify that the "Date" text is displayed
        composeTestRule.onNodeWithText("Date: 11-28-2023").assertIsDisplayed()
    }

    @Test
    fun createEditEventScreen_checkIfStartTimeIsDisplayed() {

        // Verify that the "Start time" text is displayed
        composeTestRule.onNodeWithText("Start time: 12:00").assertIsDisplayed()
    }

    @Test
    fun createEditEventScreen_checkIfEndTimeIsDisplayed() {

        // Verify that the "End time" text is displayed
        composeTestRule.onNodeWithText("End time: 12:00").assertIsDisplayed()
    }

    @Test
    fun createEditEventScreen_checkIfDescriptionIsDisplayed() {

        // Verify that the "Description" text field is displayed
        composeTestRule.onNodeWithText("Description").assertIsDisplayed()
    }

    @Test
    fun createEditEventScreen_checkIfLocationIsDisplayed() {

        // Verify that the "Location" text field is displayed
        composeTestRule.onNodeWithText("Location").assertIsDisplayed()
    }

    @Test
    fun createEditEventScreen_checkDatePickerIsShown() {

        // Click on the "Select date" button to show the date picker
        composeTestRule
            .onNodeWithText("Select date")
            .performClick()

        // Verify that the date picker is shown
        composeTestRule
            .onNodeWithContentDescription("Date picker")
            .assertIsDisplayed()
    }

    @Test
    fun createEditEventScreen_checkTimePickersAreShown() {

        // Click on the "Select start time" button to show the start time picker
        composeTestRule
            .onNodeWithText("Select start time")
            .performClick()

        // Verify that the start time picker is shown
        composeTestRule
            .onNodeWithContentDescription("Start time picker")
            .assertIsDisplayed()

        // Click on the "Select end time" button to show the end time picker
        composeTestRule
            .onNodeWithText("Select end time")
            .performClick()

        // Verify that the end time picker is shown
        composeTestRule
            .onNodeWithContentDescription("End time picker")
            .assertIsDisplayed()
    }
}