package com.example.calendar.presentation

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.calendar.ui.theme.CalendarTheme
import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calendar.data.database.Event
import com.example.calendar.data.NavRoutes
import com.example.calendar.presentation.viewmodels.CalendarViewModel
import com.example.calendar.presentation.viewmodels.EventViewModel
import com.example.calendar.presentation.screen.CalendarView
import com.example.calendar.presentation.screen.CreateEditEventScreen
import com.example.calendar.presentation.screen.DailyOverview
import com.example.calendar.presentation.viewmodels.DailyViewModel
import com.example.calendar.presentation.screen.MonthView
import com.example.calendar.presentation.screen.ViewEventScreen
import com.example.calendar.data.database.AppDatabase
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale



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

                    // Pass the database instance to the CalendarApp function
                    CalendarApp(eventviewModel = EventViewModel(database = database), dayviewModel = DailyViewModel(database = database), navController = rememberNavController(),calendarModel = CalendarViewModel(database), database = database)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun CalendarApp(eventviewModel: EventViewModel, dayviewModel: DailyViewModel, navController: NavHostController = rememberNavController(),calendarModel: CalendarViewModel, database: AppDatabase) {

        val currentDateTime = LocalDateTime.now()
        val dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
        val currentDate = currentDateTime.format(dateFormatter)
        // populate the database with some events

        val event1 = Event(
            title = "CS 407",
            date = currentDate,
            startTime = "12:00",
            endTime = "13:00",
            description = "Software Engineering",
            location = "Online",
            course = "CS 407"
        )
//        val event2 = Event(
//            title = "CS 407",
//            date = currentDate,
//            startTime = "12:00",
//            endTime = "13:00",
//            description = "Software Engineering",
//            location = "Online",
//            course = "CS 407"
//        )
//        val event3 = Event(
//            title = "CS 407",
//            date = "12-01-2023",
//            startTime = "12:00",
//            endTime = "13:00",
//            description = "Software Engineering",
//            location = "Online",
//            course = "CS 407"
//        )

        eventviewModel.addToList(event1, database)
//        eventviewModel.addToList(event2, database)
//        eventviewModel.addToList(event3, database)


        NavHost(navController = navController, startDestination = NavRoutes.CalendarView.route) {
            composable(NavRoutes.CalendarView.route) {
                CalendarView(navController = navController, calendarModel = calendarModel, eventviewModel, dayviewModel, database)
            }
            composable(NavRoutes.CreateEvent.route) {
                var event by remember { mutableStateOf<com.example.calendar.data.database.Event?>(null) }
                LaunchedEffect(key1 = Unit) {
                    // convert eventViewModel.selectedEvent to event
                    event = withContext(Dispatchers.IO) {
                        eventviewModel.selectedEvent?.let {
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
                    CreateEditEventScreen(eventviewModel, navController = navController, inputDate = date, inputEvent = event!!, database)
                }
            }
            composable(NavRoutes.EditEvent.route) {
                eventviewModel.selectedEvent?.let { event ->
                    CreateEditEventScreen(eventviewModel, navController = navController, inputDate = eventviewModel.selectedEvent!!.date,
                        inputEvent = event, database
                    )
                }
            }
            composable(NavRoutes.EventView.route) {
                ViewEventScreen(eventviewModel, navController = navController, database)
            }
            composable(NavRoutes.DayView.route + "/{date}") { navBackStackEntry ->
                // Retrieve the date from the route's arguments
                val date = remember {
                    navBackStackEntry.arguments?.getString("date") ?: currentDate
                }
                val format = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
                val selectedDate = format.parse(date)

                DailyOverview(navController, calendarModel,dayviewModel, eventviewModel, database)
            }
            composable(NavRoutes.MonthView.route) {
                MonthView(navController = navController, calendarModel = calendarModel, eventviewModel, database)
            }
        }
    }
}