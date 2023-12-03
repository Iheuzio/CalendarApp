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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.calendar.R
import com.example.calendar.data.NavRoutes
import com.example.calendar.data.database.AppDatabase
import com.example.calendar.presentation.getStringResource
import com.example.calendar.presentation.viewmodels.EventViewModel

/**
 * Screen to view a single event's details and be able to delete it
 * @param viewModel EventViewModel
 * @param navController
 */
@Composable
fun ViewEventScreen(viewModel: EventViewModel, navController: NavController, database: AppDatabase) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
viewModel.selectedEvent?.let { Text(LocalContext.current.getStringResource(R.string.title) + ": ${it.title}") }
viewModel.selectedEvent?.let { Text(LocalContext.current.getStringResource(R.string.select_date) + ": ${it.date}") }
viewModel.selectedEvent?.let { Text(LocalContext.current.getStringResource(R.string.select_start_time) + ": ${it.startTime}") }
viewModel.selectedEvent?.let { Text(LocalContext.current.getStringResource(R.string.select_end_time) + ": ${it.endTime}") }
viewModel.selectedEvent?.let { Text(LocalContext.current.getStringResource(R.string.description) + ": ${it.description}") }
viewModel.selectedEvent?.let { Text(LocalContext.current.getStringResource(R.string.location) + ": ${it.location}") }
viewModel.selectedEvent?.let { Text(LocalContext.current.getStringResource(R.string.course) + ": ${it.course}") }

        Button(
            onClick = {
                viewModel.selectedEvent?.let { event ->
                    viewModel.removeFromList(event.id, database)
                }
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