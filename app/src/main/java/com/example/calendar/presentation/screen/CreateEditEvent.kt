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
import androidx.compose.runtime.*
import androidx.compose.ui.platform.testTag
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CreateEditEventScreen(
    viewModel: EventViewModel,
    navController: NavController,
    inputDate: String,
    inputEvent: Event
) {
    var date by remember { mutableStateOf(inputDate) }
    var startTime by remember { mutableStateOf(inputEvent.startTime) }
    var endTime by remember { mutableStateOf(inputEvent.endTime) }
    var title by remember { mutableStateOf(inputEvent.title) }
    var description by remember { mutableStateOf(inputEvent.description) }
    var location by remember { mutableStateOf(inputEvent.location) }
    var course by remember { mutableStateOf(inputEvent.course) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        //ALl fields for creating/editing events
        TitleInput(title) { title = it }
        DateInput(date) { date = it }
        StartTimePicker(inputEvent.startTime) { startTime = it }
        EndTimePicker(inputEvent.endTime, startTime) { endTime = it }
        DescriptionInput(description) { description = it }
        LocationInput(location) { location = it }
        CourseInput(course) { course = it }

        //Button for saving changes/creating event
        SaveChangesButton(viewModel, navController, inputEvent,
            date, startTime, endTime, title, description, location, course
        )

        //To go back to day view or month view
        BackButton(navController)
    }
}

/**
 * Text field for title of event
 * @param title
 * @param onTitleChange
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleInput(title: String, onTitleChange: (String) -> Unit) {
    TextField(
        value = title,
        modifier = Modifier.testTag("TITLE"),
        onValueChange = { onTitleChange(it) },
        label = { Text("Title") }
    )
}

/**
 * To select a different date for an event
 * @param date
 * @param onDateChange
 */
@Composable
fun DateInput(date: String, onDateChange: (String) -> Unit) {
    Text("Date: $date")

    //Format the date string into array
    var dateValues = date.split("-")
    if (dateValues.size == 1) {
        dateValues = date.split("/")
    }
    //Create dialog to use for picking date
    val datePicker = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            onDateChange("${selectedMonth + 1}-$selectedDayOfMonth-$selectedYear")
        }, dateValues[2].toInt(), dateValues[0].toInt(), dateValues[1].toInt()
    )

    Button(
        onClick = {
            datePicker.show()
        }
    ) {
        Text(text = "Select date")
    }
}

/**
 * To select a different start time for event
 * @param initialStartTime
 * @param onStartTimeChange
 */
@Composable
fun StartTimePicker(initialStartTime: String, onStartTimeChange: (String) -> Unit) {
    var startTime by remember { mutableStateOf(initialStartTime) }

    val startTimeValues = initialStartTime.split(":")
    //Create dialog to use for picking start time
    val startTimePicker = TimePickerDialog(
        LocalContext.current,
        { _, selectedHour: Int, selectedMinute: Int ->
            startTime = "$selectedHour:$selectedMinute"
            onStartTimeChange(startTime) // Update the callback with the new value
        }, startTimeValues[0].toInt(), startTimeValues[1].toInt(), false
    )

    Text("Start time: $startTime")
    Button(
        onClick = {
            startTimePicker.show()
        }
    ) {
        Text(text = "Select start time")
    }
}

/**
 * To select a different end time for event
 * @param initialEndTime
 * @param onEndTimeChange
 */
@Composable
fun EndTimePicker(initialEndTime: String, startTime: String, onEndTimeChange: (String) -> Unit) {
    var endTime by remember { mutableStateOf(initialEndTime) }

    val endTimeValues = initialEndTime.split(":")
    //Create dialog to use for picking date
    val endTimePicker = TimePickerDialog(
        LocalContext.current,
        { _, selectedHour: Int, selectedMinute: Int ->
            val selectedEndTime = "$selectedHour:$selectedMinute"
            if (isValidEndTime(startTime, selectedEndTime)) {
                endTime = selectedEndTime
                onEndTimeChange(endTime) // Update the callback with the new value
            }
        },
        endTimeValues[0].toInt(),
        endTimeValues[1].toInt(),
        false
    )

    Text("End time: $endTime")
    Button(
        onClick = {
            endTimePicker.show()
        }
    ) {
        Text(text = "Select end time")
    }
}

/**
 * Text field to input description
 * @param description
 * @param onDescriptionChange
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescriptionInput(description: String, onDescriptionChange: (String) -> Unit) {
    TextField(
        value = description,
        modifier = Modifier.testTag("DESCRIPTION"),
        onValueChange = { onDescriptionChange(it) },
        label = { Text("Description") }
    )
}

/**
 * Text field to input location
 * @param location
 * @param onLocationChange
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationInput(location: String, onLocationChange: (String) -> Unit) {
    TextField(
        value = location,
        modifier = Modifier.testTag("LOCATION"),
        onValueChange = { onLocationChange(it) },
        label = { Text("Location") }
    )
}

/**
 * Text field to input course
 * @param course
 * @param onCourseChange
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseInput(course: String, onCourseChange: (String) -> Unit) {
    TextField(
        value = course,
        modifier = Modifier.testTag("COURSE"),
        onValueChange = { onCourseChange(it) },
        label = { Text("Course") }
    )
}

/**
 * Button to save changes
 * @param viewModel
 * @param navController
 * @param inputEvent
 * @param date, startTime, endTime, title, description, location, course for creating/editing event
 */
@Composable
fun SaveChangesButton(
    viewModel: EventViewModel, navController: NavController, inputEvent: Event,
    date: String, startTime: String, endTime: String, title: String,
    description: String, location: String, course: String
) {
    Button(
        onClick = {
            //Check if there is a selected event to modify event
            if (viewModel.selectedEvent != null) {
                viewModel.modifyItem(
                    viewModel.selectedEvent!!,
                    Event(inputEvent.id, date, startTime, endTime, title, description, location, course)
                )
            } else {
                //Otherwise create an event
                viewModel.addToList(
                    Event(viewModel.idCount, date, startTime, endTime, title, description, location, course
                    )
                )
                //Increment id for next event creation
                viewModel.incrementId()
            }
            //Navigate back to where the user was when pressing the create/edit event button
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
}

/**
 * To navigate back to where the user was when pressing the create/edit event button
 * @param navController
 */
@Composable
fun BackButton(navController: NavController) {
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

//Checks if the end time is before the start time to validate
fun isValidEndTime(startTime: String, endTime: String): Boolean {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    val startCalendar = Calendar.getInstance()
    val endCalendar = Calendar.getInstance()

    startCalendar.time = sdf.parse(startTime)!!
    endCalendar.time = sdf.parse(endTime)!!

    return startCalendar.before(endCalendar)
}

@Preview(showBackground = true)
@Composable
fun CreateEditEventPreview() {
    CalendarTheme {
        val date = "01-08-2023"
        val event = Event(0, date, "12:43", "12:43")
        val navController = rememberNavController()
        val viewModel = EventViewModel()
        CreateEditEventScreen(viewModel, navController, date, event)
    }
}
