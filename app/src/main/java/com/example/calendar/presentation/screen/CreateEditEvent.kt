package com.example.calendar.presentation.screen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.example.calendar.data.Event
import com.example.calendar.data.NavRoutes
import com.example.calendar.presentation.viewmodels.EventViewModel
import com.example.calendar.ui.theme.CalendarTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditEventScreen(viewModel: EventViewModel, navController: NavController, inputDate: String,
                          inputEvent: Event
) {

    // State variables (to be moved into ViewModel)
    var date by remember { mutableStateOf(inputDate) }
    var startTime by remember { mutableStateOf(inputEvent.startTime) }
    var endTime by remember { mutableStateOf(inputEvent.endTime) }
    var title by remember { mutableStateOf(inputEvent.title) }
    var description by remember { mutableStateOf(inputEvent.description) }
    var location by remember { mutableStateOf(inputEvent.location) }

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
        var dateValues = inputDate.split("-")
        if (dateValues.size == 1) {
            dateValues = inputDate.split("/")
        }
        val datePicker = DatePickerDialog(
            LocalContext.current,
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
                date = "${selectedMonth+1}-$selectedDayOfMonth-$selectedYear"
            }, dateValues[2].toInt(), dateValues[0].toInt(), dateValues[1].toInt()
        )

        Button(
            onClick = {
                datePicker.show()
            }
        ) {
            Text(text = "Select date")
        }

        //Select Start Time
        //Assuming that hour is passed in as a string such as "12:45"
        val startTimeValues = inputEvent.startTime.split(":")
        val startTimePicker = TimePickerDialog(
            LocalContext.current,
            { _, selectedHour: Int, selectedMinute: Int ->
                startTime = "$selectedHour:$selectedMinute"
            }, startTimeValues[0].toInt(), startTimeValues[1].toInt(), false
        )

        //Button to show TimePickerDialog
        Text("Start time: $startTime")
        Button(
            onClick = {
                startTimePicker.show()
            }
        ) {
            Text(text = "Select start time")
        }

        //Select End Time
        //Assuming that hour is passed in as a string such as "12:45"
        val endTimeValues = inputEvent.endTime.split(":")
        val endTimePicker = TimePickerDialog(
            LocalContext.current,
            { _, selectedHour: Int, selectedMinute: Int ->
                endTime = "$selectedHour:$selectedMinute"
            }, endTimeValues[0].toInt(), endTimeValues[1].toInt(), false
        )


        Text("End time: $endTime")
        Button(
            onClick = {
                endTimePicker.show()
            }
        ) {
            Text(text = "Select end time")
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
                    viewModel.modifyItem(
                        viewModel.selectedEvent!!
                    ,
                        Event(inputEvent.id, date, startTime, endTime, title, description, location)
                    )
                }
                //If they're creating an event
                else {
                    viewModel.addToList(Event(viewModel.idCount, date, startTime, endTime, title, description, location))
                    viewModel.incrementId()
                }
                //Save changes and pop back navigation to start
                navController.navigate(NavRoutes.CalendarView.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                        inclusive = true
                    }
                }
            }
        ) {
            Text("Save changes")
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
            Text("Back")
        }

    }
}
@Preview(showBackground = true)
@Composable
fun CreateEditEventPreview() {
    CalendarTheme {
        val date = "01/08/2023"
        val event = Event(0, date, "12:43", "12:43")
        val navController = rememberNavController()
        val viewModel = EventViewModel()
        CreateEditEventScreen(viewModel, navController, date, event)
    }
}
