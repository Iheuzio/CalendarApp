package com.example.calendar.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.NavType
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
import com.example.calendar.presentation.viewmodels.DailyViewModel
import com.example.calendar.presentation.viewmodels.EventViewModel
import com.example.calendar.ui.theme.CalendarTheme
import kotlinx.coroutines.Dispatchers
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
        requestLocationPermission()
        setContent {
            CalendarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Create an instance of the AppDatabase
                    val database = AppDatabase.getInstance(this)

                    // Pass the database instance to the CalendarApp function
                    // these are the viewmodels for the different screens
                    CalendarApp(eventviewModel = EventViewModel(database = database), dayviewModel = DailyViewModel(), navController = rememberNavController(),calendarModel = CalendarViewModel(database), database = database)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun CalendarApp(eventviewModel: EventViewModel, dayviewModel: DailyViewModel, navController: NavHostController = rememberNavController(),calendarModel: CalendarViewModel, database: AppDatabase) {

        NavHost(navController = navController, startDestination = NavRoutes.CalendarView.route) {
            composable(NavRoutes.CalendarView.route) {
                CalendarView(navController = navController, calendarModel = calendarModel, eventviewModel, dayviewModel, database)
            }
            composable(NavRoutes.CreateEvent.route) {
                var event by remember { mutableStateOf<Event?>(null) }
                LaunchedEffect(key1 = Unit) {
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
            composable(NavRoutes.DayView.route + "/{date}") {
                DailyOverview(navController, calendarModel,dayviewModel, eventviewModel, database)
            }
            composable(NavRoutes.MonthView.route) {
                MonthView(navController = navController, calendarModel = calendarModel, eventviewModel, database)
            }

            composable(
                route = "fiveDayForecast/{latitude}/{longitude}",
                arguments = listOf(
                    navArgument("latitude") { type = NavType.FloatType },
                    navArgument("longitude") { type = NavType.FloatType }
                )
            ) { backStackEntry ->
                val latitude = backStackEntry.arguments?.getFloat("latitude") ?: 0f
                val longitude = backStackEntry.arguments?.getFloat("longitude") ?: 0f
                FiveDayForecastScreen(
                    navController = navController,
                    latitude = latitude.toDouble(),
                    longitude = longitude.toDouble()
                )
            }

        }
    }
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }
    @Suppress("DEPRECATION")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
            }
        }
    }

}