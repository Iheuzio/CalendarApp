package com.example.calendar

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.calendar.ui.theme.CalendarTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditEventScreen(inputDate: String, inputTime: String, inputTitle: String = "",
                          inputDescription: String = "", inputLocation: String = "") {
    var date = inputDate
    var time = inputTime
    var title = inputTitle
    var description = inputDescription
    var location = inputLocation

    Column {
        //Title input
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(title) }
        )

        //Date input (date picker or based on what dates there are in calendar?)
        Text("Date: $date")
        //Showing a small calendar screen here to pick the date would be cute, or maybe like sliding up down for date
            //(kind of like select)
        Button(
            onClick = {
                date = ""
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
            label = { Text(description) }
        )

        //Location input
        TextField(
            value = location,
            onValueChange = { location = it },
            label = { Text(location) }
        )

        //To save changes
        Button(
            onClick = {
                //Save changes and pop back navigation to start
                /*navController.navigate(NavRoutes.Home.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                    inclusive = true
                }
            }*/
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
        CreateEditEventScreen(date, time)
    }
}
