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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calendar.data.Event
import com.example.calendar.data.NavRoutes
import com.example.calendar.presentation.viewmodels.CalendarViewModel
import com.example.calendar.presentation.viewmodels.EventViewModel
import com.example.calendar.presentation.screen.CalendarView
import com.example.calendar.presentation.screen.CreateEditEventScreen
import com.example.calendar.presentation.screen.DailyOverview
import com.example.calendar.presentation.viewmodels.DailyViewModel
import com.example.calendar.presentation.screen.MonthView
import com.example.calendar.presentation.screen.ViewEventScreen
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
                    // remove this if wanting to test your event stuff, just uncomment 
                    //CalendarView()
                    //DailyOverviewScreen(null, null, null, null);
                    //Greeting("Android")
                    // remove this if wanting to test your event stuff, just uncomment

                    //Greeting("Android")
                // CreateEditEventScreen(inputDate = "01/08/2023", inputTime = "9:22")
                    //CalendarView()
                    //CreateEditEventScreen(inputDate = "01/08/2023", inputTime = "9:22")
                    CalendarApp()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun CalendarApp(eventviewModel: EventViewModel = EventViewModel(), dayviewModel: DailyViewModel = DailyViewModel(), navController: NavHostController = rememberNavController()) {

        val currentDateTime = LocalDateTime.now()
        val dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
        val currentDate = currentDateTime.format(dateFormatter)

       val calendarModel by viewModels<CalendarViewModel>()
        NavHost(navController = navController, startDestination = NavRoutes.CalendarView.route) {
            composable(NavRoutes.CalendarView.route) {
                CalendarView(navController = navController, calendarModel = calendarModel, eventviewModel, dayviewModel)
            }
            composable(NavRoutes.CreateEvent.route)
            {
                val event = Event(eventviewModel.idCount, currentDate, "12:00", "12:00")
                eventviewModel.incrementId()
                val format = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())
                val date = format.format(calendarModel.selectedDate.value)
                CreateEditEventScreen(eventviewModel, navController = navController, inputDate = date, event)
            }
            composable(NavRoutes.EditEvent.route) {
                eventviewModel.selectedEvent?.let { event ->
                    CreateEditEventScreen(eventviewModel, navController = navController, inputDate = eventviewModel.selectedEvent!!.date,
                        event
                    )
                }
            }
            composable(NavRoutes.EventView.route) {
                ViewEventScreen(eventviewModel, navController = navController)
            }
            composable(NavRoutes.DayView.route + "/{date}") { navBackStackEntry ->
                // Retrieve the date from the route's arguments
                val date = remember {
                    navBackStackEntry.arguments?.getString("date") ?: currentDate
                }
                val format = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
                val selectedDate = format.parse(date)

                DailyOverview(navController, calendarModel,dayviewModel, eventviewModel)
            }
            composable(NavRoutes.MonthView.route) {
                MonthView(navController = navController, calendarModel = calendarModel, eventviewModel)
            }
        }
    }
}




