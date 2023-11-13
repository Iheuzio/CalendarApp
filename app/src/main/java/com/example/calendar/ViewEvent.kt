package com.example.calendar

import androidx.compose.material3.Button
import androidx.navigation.NavController
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination

@Composable
fun ViewEventScreen(navController: NavController, inputDate: String, inputTime: String, inputTitle: String ,
                inputDescription: String, inputLocation: String) {
    Text(inputTitle)
    Text(inputDate)
    Text(inputTime)
    Text(inputDescription)
    Text(inputLocation)

    Button(
        onClick = {
            //Save changes and pop back navigation to start
            navController.navigate(NavRoutes.CalendarView.route) {
                //change so it goes back to where it was before
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