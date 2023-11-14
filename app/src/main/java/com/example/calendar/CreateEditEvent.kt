package com.example.calendar

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calendar.ui.theme.CalendarTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditEventScreen(viewModel: EventViewModel, navController: NavController, inputDate: String,
                          inputTime: String, inputTitle: String = "",
                          inputDescription: String = "", inputLocation: String = "") {

    // State variables (to be moved into ViewModel)
    var date by rememberSaveable { mutableStateOf(inputDate) }
    var time by rememberSaveable { mutableStateOf(inputTime) }
    var title by rememberSaveable { mutableStateOf(inputTitle) }
    var description by rememberSaveable { mutableStateOf(inputDescription) }
    var location by rememberSaveable { mutableStateOf(inputLocation) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        //Title input
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") }
        )

        //Date input (date picker or based on what dates there are in calendar?)
        Text("Date: $date")
        val dateValues = inputDate.split("/")
        val datePicker = DatePickerDialog(
            LocalContext.current,
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
                date = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
            }, dateValues[2].toInt(), dateValues[0].toInt(), dateValues[1].toInt()
        )
        Button(
            onClick = {
                datePicker.show()
            }
        ) {
            Text(text = "Select date")
        }

        //Select Time
        //Assuming that hour is passed in as a string such as "12:45"
        val timeValues = inputTime.split(":")
        val timePicker = TimePickerDialog(
            LocalContext.current,
            { _, selectedHour: Int, selectedMinute: Int ->
                time = "$selectedHour:$selectedMinute"
            }, timeValues[0].toInt(), timeValues[1].toInt(), false
        )

        //Button to show TimePickerDialog
        Text("Time: $time")
        Button(
            onClick = {
                timePicker.show()
            }
        ) {
            Text(text = "Select time")
        }

        //Description input
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") }
        )

        //Location input
        TextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") }
        )

        //Button to save changes
        Button(
            onClick = {
                //If user is editing an event
                if (viewModel.selectedEvent != null) {
                    viewModel.modifyItem(Event(
                        inputDate,
                        inputTitle,
                        inputDescription,
                        inputLocation
                    ),
                        Event(date, time, title, description, location))
                }
                //If they're creating an event
                else {
                    viewModel.addToList(Event(date, time, title, description, location))
                }
                //Save changes and pop back navigation to start
                navController.navigate(NavRoutes.CalendarView.route) {
                    //TO DO: change so it goes back to the day it's scheduled to
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                        inclusive = true
                    }
                }
            }
        ) {
            Text("Save changes")
        }
    }

}
@Preview(showBackground = true)
@Composable
fun CreateEditEventPreview() {
    CalendarTheme {
        val date = "01/08/2023"
        val time = "12:43"
        val navController = rememberNavController()
        val viewModel = EventViewModel()
        CreateEditEventScreen(viewModel, navController, date, time)
    }
}
