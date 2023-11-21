package com.example.calendar.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.navigation.NavController
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.example.calendar.R
import com.example.calendar.data.NavRoutes
import com.example.calendar.presentation.viewmodels.EventViewModel
import com.example.calendar.ui.theme.CalendarTheme

/**
 * Screen to view a single event's details and be able to delete it
 * @param viewModel EventViewModel
 * @param navController
 */
@Composable
fun ViewEventScreen(viewModel: EventViewModel, navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        viewModel.selectedEvent?.let { Text(LocalContext.current.getStringResource(R.string.title) + ": ${it.title}") }
        viewModel.selectedEvent?.let { Text("Date: ${it.date}") }
        viewModel.selectedEvent?.let { Text("Start time: ${it.startTime}") }
        viewModel.selectedEvent?.let { Text("End time: ${it.endTime}") }
        viewModel.selectedEvent?.let { Text("Description: ${it.description}") }
        viewModel.selectedEvent?.let { Text("Location: ${it.location}") }
        viewModel.selectedEvent?.let { Text("Course: ${it.course}") }

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
            Text(stringResource(R.string.delete))
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
            Text(stringResource(R.string.done))
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