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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.example.calendar.R
import com.example.calendar.data.NavRoutes
import com.example.calendar.data.database.AppDatabase
import com.example.calendar.presentation.getStringResource
import com.example.calendar.presentation.viewmodels.EventViewModel
import com.example.calendar.ui.theme.CalendarTheme
import kotlinx.coroutines.launch

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
                    viewModel.viewModelScope.launch {
                        database.eventDao().delete(event)
                    }
                    viewModel.removeFromList(event, database)
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

@Composable
fun ViewEventPreview(database: AppDatabase) {
    CalendarTheme {
        val navController = rememberNavController()
        val viewModel = EventViewModel(database = database)
        ViewEventScreen(viewModel, navController, database)
    }
}