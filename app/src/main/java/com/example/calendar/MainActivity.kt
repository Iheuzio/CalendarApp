package com.example.calendar

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendar.ui.theme.CalendarTheme
import java.text.SimpleDateFormat
import java.util.*
import androidx.annotation.StringRes
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter



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

        NavHost(navController = navController, startDestination = NavRoutes.CalendarView.route) {
            composable(NavRoutes.CalendarView.route) {
                CalendarView(viewModel, navController = navController)
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
                    onEventSelected = { event ->
                        // handle event selected action
                        viewModel.selectedEvent = event
                        navController.navigate(NavRoutes.EventView.route)
                    },
                    onAddEvent = {
                        navController.navigate(NavRoutes.CreateEvent.route)
                    },
                    onChangeDate = { newDate ->
                        // Update the date variable when onChangeDate is called
                        navController.navigate(NavRoutes.DayView.route + "/$newDate")
                    },
                    onEditEvent = { event ->
                        viewModel.selectedEvent = event
                        navController.navigate(NavRoutes.EditEvent.route)
                    }
                )
            }

        }
    }
}




