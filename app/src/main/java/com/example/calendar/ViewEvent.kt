package com.example.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.navigation.NavController
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.example.calendar.ui.theme.CalendarTheme

@Composable
fun ViewEventScreen(viewModel: EventViewModel, navController: NavController) {
    Column {
        viewModel.selectedEvent?.let { Text("Title: " + it.title) }
        viewModel.selectedEvent?.let { Text("Date: " + it.date) }
        viewModel.selectedEvent?.let { Text("Start time: " + it.startTime) }
        viewModel.selectedEvent?.let { Text("End time: " + it.endTime) }
        viewModel.selectedEvent?.let { Text("Description: " + it.description) }
        viewModel.selectedEvent?.let { Text("Location: " + it.location) }

        Button(
            onClick = {
                viewModel.selectedEvent?.let { viewModel.removeFromList(it) }
                //Save changes and pop back navigation to start
                navController.navigate(NavRoutes.CalendarView.route) {
                    //TO DO: change so it goes back to where it was before
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                        inclusive = true
                    }
                }
                //TO DO: Alert that event has been deleted once navigated back
            }
        ) {
            Text("Delete")
        }
        Button(
            onClick = {
                //TO DO: change so it goes back to the day it's scheduled to
                //Save changes and pop back navigation to start
                navController.navigate(NavRoutes.CalendarView.route) {
                    //TO DO: change so it goes back to where it was before
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                        inclusive = true
                    }
                }
            }
        ) {
            Text("Done")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ViewEventPreview() {
    CalendarTheme {
        val navController = rememberNavController()
        val viewModel = EventViewModel()
        ViewEventScreen(viewModel, navController)
    }
}