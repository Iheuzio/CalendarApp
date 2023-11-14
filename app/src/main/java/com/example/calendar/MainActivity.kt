package com.example.calendar

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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



fun Context.getStringResource(@StringRes resId: Int): String {
    return resources.getString(resId)
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalendarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
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

    @Composable
    fun CalendarApp(viewModel: EventViewModel = EventViewModel(), navController: NavHostController = rememberNavController()) {
       val calendarModel by viewModels<CalendarViewModel>()
        NavHost(navController = navController, startDestination = NavRoutes.CalendarView.route) {
            composable(NavRoutes.CalendarView.route) {
                CalendarView(navController = navController, calendarModel = calendarModel)
            }
            composable(NavRoutes.CreateEditEvent.route) {
                CreateEditEventScreen(viewModel, navController = navController, inputDate = "01/08/2023", inputTime = "9:22")
            }
        }
    }
}




