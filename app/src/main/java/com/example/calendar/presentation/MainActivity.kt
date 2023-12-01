package com.example.calendar.presentation

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.calendar.ui.theme.CalendarTheme
import androidx.annotation.StringRes
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.calendar.data.database.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


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

        NavHost(navController = navController, startDestination = NavRoutes.CalendarView.route) {
            composable(NavRoutes.CalendarView.route) {
                CalendarView(navController = navController, calendarModel = calendarModel, eventviewModel, dayviewModel, database)
            }
            composable(NavRoutes.CreateEvent.route)
            {
                val format = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())
                val date = format.format(calendarModel.selectedDate.value)
                CreateEditEventScreen(eventviewModel, navController = navController, inputDate = date, inputEvent = null, database)
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