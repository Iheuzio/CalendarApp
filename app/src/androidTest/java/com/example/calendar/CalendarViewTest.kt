import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.testing.TestNavHostController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.calendar.CalendarView
import com.example.calendar.CalendarViewModel
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

    @Before
    fun setup() {
        navController = TestNavHostController(InstrumentationRegistry.getInstrumentation().targetContext)
        calendarModel = CalendarViewModel()
        composeTestRule.setContent {
            CalendarView(navController, calendarModel)
        }
    }

    @Test
    fun calendarView_displaysMonthView_whenShowDailyOverviewIsFalse() {
        calendarModel.showDailyOverview.value = false
        composeTestRule.onNodeWithText("Month View").assertIsDisplayed()
    }

    @Test
    fun calendarView_displaysDailyOverview_whenShowDailyOverviewIsTrue() {
        calendarModel.showDailyOverview.value = true
        composeTestRule.onNodeWithText("Daily Overview").assertIsDisplayed()
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
}