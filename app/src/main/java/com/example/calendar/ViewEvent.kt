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
fun ViewEventScreen(viewModel: EventViewModel, navController: NavController,
                    inputDate: String, inputTime: String, inputTitle: String ,
                    inputDescription: String, inputLocation: String) {
    Column {
        Text(inputTitle)
        Text(inputDate)
        Text(inputTime)
        Text(inputDescription)
        Text(inputLocation)

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
        val date = "01/08/2023"
        val time = "12:43"
        val navController = rememberNavController()
        val viewModel = EventViewModel()
        ViewEventScreen(viewModel, navController, "12/09/2023", "12:45", "title",
            "description", "location")
    }
}