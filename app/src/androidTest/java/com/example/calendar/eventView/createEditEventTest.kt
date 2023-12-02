package com.example.calendar.eventView

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.navigation.testing.TestNavHostController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.calendar.presentation.screen.CreateEditEventScreen
import com.example.calendar.presentation.screen.isValidEndTime
import com.example.calendar.presentation.viewmodels.EventViewModel
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
                inputEvent = Event(0, "11-28-2023", "12:00", "12:00", "")
            )
        }
    }

    @Test
    fun testIsValidEndTime() {
        assertFalse(isValidEndTime("13:00", "12:00"))
        assert(isValidEndTime("12:00", "13:00"))
    }

    @Test
    fun createEditEventScreen_checkTitleTextField() {
        // Verify that the Title text field is displayed
        composeTestRule.onNodeWithText("Title").assertIsDisplayed()

        // Sets the TextField value
        composeTestRule.onNodeWithTag("TITLE").performTextInput("sample title")

        // Asserts the TextField has the corresponding value
        composeTestRule.onNodeWithTag("TITLE").assert(hasText("sample title"))
    }

    @Test
    fun createEditEventScreen_checkIfDateIsDisplayed() {

        // Verify that the Date text is displayed
        composeTestRule.onNodeWithText("Date: 11-28-2023").assertIsDisplayed()
    }

    @Test
    fun createEditEventScreen_checkIfStartTimeIsDisplayed() {

        // Verify that the Start time text is displayed
        composeTestRule.onNodeWithText("Start time: 12:00").assertIsDisplayed()
    }

    @Test
    fun createEditEventScreen_checkIfEndTimeIsDisplayed() {

        // Verify that the End time text is displayed
        composeTestRule.onNodeWithText("End time: 12:00").assertIsDisplayed()
    }

    @Test
    fun createEditEventScreen_checkDescriptionTextField() {
        // Verify that the Description text field is displayed
        composeTestRule.onNodeWithText("Description").assertIsDisplayed()

        // Sets the TextField value
        composeTestRule.onNodeWithTag("DESCRIPTION").performTextInput("sample description")

        // Asserts the TextField has the corresponding value
        composeTestRule.onNodeWithTag("DESCRIPTION").assert(hasText("sample description"))
    }

    @Test
    fun createEditEventScreen_checkLocationTextField() {
        // Verify that the Location text field is displayed
        composeTestRule.onNodeWithText("Location").assertIsDisplayed()

        // Sets the TextField value
        composeTestRule.onNodeWithTag("LOCATION").performTextInput("sample location")

        // Asserts the TextField has the corresponding value
        composeTestRule.onNodeWithTag("LOCATION").assert(hasText("sample location"))
    }

    @Test
    fun createEditEventScreen_checkCourseTextField() {
        // Verify that the Course text field is displayed
        composeTestRule.onNodeWithText("Course").assertIsDisplayed()

        // Sets the TextField value
        composeTestRule.onNodeWithTag("COURSE").performTextInput("sample course")

        // Asserts the TextField has the corresponding value
        composeTestRule.onNodeWithTag("COURSE").assert(hasText("sample course"))
    }


}