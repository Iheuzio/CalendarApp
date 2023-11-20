package com.example.calendar

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.navigation.navArgument
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
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
    fun CalendarApp(viewModel: EventViewModel = EventViewModel(), navController: NavHostController = rememberNavController()) {

        val currentDateTime = LocalDateTime.now()
        val dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("H:mm")

        //TO DO: change after so its currently selected date and time (once merged)
        val currentDate = currentDateTime.format(dateFormatter)
        val currentTime = currentDateTime.format(timeFormatter)

       val calendarModel by viewModels<CalendarViewModel>()
        NavHost(navController = navController, startDestination = NavRoutes.CalendarView.route) {
            composable(NavRoutes.CalendarView.route) {
                CalendarView(navController = navController, calendarModel = calendarModel, viewModel)
            }
            composable(NavRoutes.CreateEvent.route) {
                val event = Event(viewModel.idCount, currentDate, "12:00", "12:00")
                viewModel.incrementId()
                CreateEditEventScreen(viewModel, navController = navController, inputDate = currentDate, event)
            }
            composable(NavRoutes.EditEvent.route) {
                viewModel.selectedEvent?.let { event ->
                    CreateEditEventScreen(viewModel, navController = navController, inputDate = currentDate,
                        event
                    )
                }
            }
            composable(NavRoutes.EventView.route) {
                ViewEventScreen(viewModel, navController = navController)
            }
            composable(NavRoutes.DayView.route + "/{date}") { navBackStackEntry ->
                // Retrieve the date from the route's arguments
                val date = remember {
                    navBackStackEntry.arguments?.getString("date") ?: currentDate
                }
                val format = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
                val selectedDate = format.parse(date)

                DailyOverviewScreen(
                    viewModel,
                    selectedDate = selectedDate,
                    events = calendarModel.events.value,
                    onEventSelected = { event ->
                        // handle event selected action
                        viewModel.selectedEvent = event
                        navController.navigate(NavRoutes.EventView.route)
                    },
                    onAddEvent = {
                        navController.navigate(NavRoutes.CreateEvent.route)
                    },
                    onChangeDate = { newDate ->
                        //navController.navigate(NavRoutes.DayView.route + "/$newDate")
                        val calendar = Calendar.getInstance()
                        calendar.time = newDate
                        calendarModel.onDateChange(calendar)
                    },
                    onBack = {
                        calendarModel.toggleShowDailyOverview()
                    },
                    onEditEvent = { event ->
                        viewModel.selectedEvent = event
                        navController.navigate(NavRoutes.EditEvent.route)
                    }

                )
            }
            composable(NavRoutes.MonthView.route) {
                MonthView(navController = navController, calendarModel = calendarModel)
            }
        }
    }
}




