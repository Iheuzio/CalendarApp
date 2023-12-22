package com.example.calendar.presentation

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.calendar.data.NavRoutes
import com.example.calendar.data.database.AppDatabase
import com.example.calendar.data.database.Event
import com.example.calendar.presentation.screen.CalendarView
import com.example.calendar.presentation.screen.CreateEditEventScreen
import com.example.calendar.presentation.screen.DailyOverview
import com.example.calendar.presentation.screen.FiveDayForecastScreen
import com.example.calendar.presentation.screen.MonthView
import com.example.calendar.presentation.screen.ViewEventScreen
import com.example.calendar.presentation.viewmodels.CalendarViewModel
import com.example.calendar.presentation.viewmodels.EventViewModel
import com.example.calendar.ui.theme.CalendarTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.navigation.NavType
import com.example.calendar.presentation.viewmodels.HolidayViewModel


fun Context.getStringResource(@StringRes resId: Int): String {
    return resources.getString(resId)
}

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalendarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Create an instance of the AppDatabase
                    val database = AppDatabase.getInstance(this)
                    val holidayModel = HolidayViewModel(database = database, context = this)

                    CalendarApp(
                        eventViewModel = EventViewModel(database = database),
                        navController = rememberNavController(),
                        calendarModel = CalendarViewModel(database),
                        database = database,
                        holidayModel = holidayModel
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun CalendarApp(eventViewModel: EventViewModel, navController: NavHostController = rememberNavController(),calendarModel: CalendarViewModel, database: AppDatabase, holidayModel: HolidayViewModel) {

        NavHost(navController = navController, startDestination = NavRoutes.CalendarView.route) {
            composable(NavRoutes.CalendarView.route) {
                CalendarView(navController = navController, calendarModel = calendarModel, eventViewModel, holidayModel)
            }
            composable(NavRoutes.CreateEvent.route) {
                var event by remember { mutableStateOf<Event?>(null) }
                LaunchedEffect(key1 = Unit) {
                    event = withContext(Dispatchers.IO) {
                        eventViewModel.selectedEvent?.let {
                            database.eventDao().getById(it.id)
                        }
                    }
                }
                if (event == null) {
                    val currentDateTime = LocalDateTime.now()
                    val dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
                    val currentDate = currentDateTime.format(dateFormatter)
                    event = Event(
                        title = "",
                        date = currentDate,
                        startTime = "12:00",
                        endTime = "1:00",
                        description = "",
                        location = "",
                        course = ""
                    )
                }
                event?.let {
                    val format = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())
                    val date = format.format(calendarModel.selectedDate.value)
                    CreateEditEventScreen(eventViewModel, navController = navController, inputDate = date, inputEvent = event!!, database)
                }
            }
            composable(NavRoutes.EditEvent.route) {
                eventViewModel.selectedEvent?.let { event ->
                    CreateEditEventScreen(eventViewModel, navController = navController, inputDate = eventViewModel.selectedEvent!!.date,
                        inputEvent = event, database
                    )
                }
            }
            composable(NavRoutes.EventView.route) {
                ViewEventScreen(eventViewModel, navController = navController, database)
            }
            composable(NavRoutes.DayView.route + "/{date}") {
                DailyOverview(navController, calendarModel, eventViewModel, holidayModel)
            }
            composable(NavRoutes.MonthView.route) {
                MonthView(navController = navController, calendarModel = calendarModel, eventViewModel, holidayModel)
            }

            composable("fiveDayForecast/{latitude}/{longitude}", arguments = listOf(
                navArgument("latitude") { type = NavType.FloatType },
                navArgument("longitude") { type = NavType.FloatType }
            )) { backStackEntry ->
                val latitude = backStackEntry.arguments?.getFloat("latitude") ?: 0f
                val longitude = backStackEntry.arguments?.getFloat("longitude") ?: 0f
                FiveDayForecastScreen(navController, latitude.toDouble(), longitude.toDouble())
            }

        }
    }

}