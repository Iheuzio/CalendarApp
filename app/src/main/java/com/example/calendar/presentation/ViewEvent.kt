package com.example.calendar.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.navigation.NavController
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.example.calendar.data.NavRoutes
import com.example.calendar.data.viewmodels.EventViewModel
import com.example.calendar.ui.theme.CalendarTheme

@Composable
fun ViewEventScreen(viewModel: EventViewModel, navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        viewModel.selectedEvent?.let { Text("Title: " + it.title) }
        viewModel.selectedEvent?.let { Text("Date: " + it.date) }
        viewModel.selectedEvent?.let { Text("Start time: " + it.startTime) }
        viewModel.selectedEvent?.let { Text("End time: " + it.endTime) }
        viewModel.selectedEvent?.let { Text("Description: " + it.description) }
        viewModel.selectedEvent?.let { Text("Location: " + it.location) }

        Button(
            onClick = {
                viewModel.selectedEvent?.let { viewModel.removeFromList(it) }
                navController.navigate(NavRoutes.CalendarView.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                        inclusive = true
                    }
                }
            }
        ) {
            Text("Delete")
        }
        Button(
            onClick = {
                navController.navigate(NavRoutes.CalendarView.route) {
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