package com.example.calendar.calendarView

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.testing.TestNavHostController
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.example.calendar.data.database.AppDatabase
import com.example.calendar.presentation.screen.CalendarView
import com.example.calendar.presentation.viewmodels.DailyViewModel
import com.example.calendar.presentation.viewmodels.CalendarViewModel
import com.example.calendar.presentation.viewmodels.EventViewModel
import com.example.calendar.presentation.viewmodels.HolidayViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class CalendarViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController
    private lateinit var calendarModel: CalendarViewModel
    private lateinit var eventViewModel : EventViewModel

    @Before
    fun setup() {
        navController = TestNavHostController(InstrumentationRegistry.getInstrumentation().targetContext)
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        calendarModel = CalendarViewModel(db)
        eventViewModel = EventViewModel(db)
        val holidayModel = HolidayViewModel(db, context)
        composeTestRule.setContent {
            CalendarView(navController, calendarModel, eventViewModel, dayModel = DailyViewModel(), db, holidayModel)
        }

    }

    @Test
    fun calendarView_displaysMonthView_whenShowDailyOverviewIsFalse() {
        calendarModel.showDailyOverview.value = false
        composeTestRule.onNodeWithText("Add Event").assertIsDisplayed()
    }

    @Test
    fun calendarView_displaysDailyOverview_whenShowDailyOverviewIsTrue() {
        calendarModel.showDailyOverview.value = true
        composeTestRule.onNodeWithText("Back").assertIsDisplayed()
    }

    @Test
    fun calendarHeader_changesMonth_whenArrowButtonsClicked() {
        val initialMonth = Calendar.getInstance().get(Calendar.MONTH)
        composeTestRule.onNodeWithContentDescription("Previous Month").performClick()
        assertNotEquals(initialMonth, calendarModel.selectedDate.value.month)
    }

    @Test
    fun calendarGrid_selectsDate_whenDateClicked() {
        val initialDate = calendarModel.selectedDate.value
        composeTestRule.onNodeWithText("1").performClick()
        assertNotEquals(initialDate, calendarModel.selectedDate.value)
    }

    @Test
    fun calendarView_displaysMonthView_whenShowDailyOverviewIsFalse_andSelectedDateChanges() {
        calendarModel.showDailyOverview.value = false
        val initialDate = calendarModel.selectedDate.value
        composeTestRule.onNodeWithText("1").performClick()
        assertNotEquals(initialDate, calendarModel.selectedDate.value)
    }
}