package com.example.calendar.eventView

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
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
    fun calendarGrid_checkIfDaySelectedInit() {
        // get current day and check if it is selected
        //val initialDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        //Assert.assertEquals(initialDay, calendarModel.selectedDate.value.date)
    }

    @Test
    fun createEditEventScreen_checkIfTitleIsDisplayed() {
        composeTestRule
            .onNodeWithText("Title")
            .assertIsDisplayed()
    }

    @Test
    fun createEditEventScreen_checkIfDateIsDisplayed() {
        composeTestRule
            .onNodeWithText("Date: 11-28-2023")
            .assertIsDisplayed()
    }
}