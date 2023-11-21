package com.example.calendar.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendar.R
import java.text.SimpleDateFormat
import java.util.Calendar

/**
 * This is the view for the month header. It displays the month and year.
 * @param calendar The calendar object used to get the month and year.
 */
@Composable
fun monthYearHeader(calendar: Calendar) {
    val dateFormat = SimpleDateFormat("MMMM yyyy")
    val monthYear = dateFormat.format(calendar.time)

    Text(
        text = monthYear,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(0.8f),
        style = TextStyle(
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 20.sp
        ),
    )
}

/**
 * This is the view for the day of the week header. It displays the day of the week names.
 */
@Composable
fun DayOfWeekHeader() {
    // use localContext to get string resources for each day of the week
    val dayNames = listOf(
        LocalContext.current.getStringResource(R.string.sunday),
        LocalContext.current.getStringResource(R.string.monday),
        LocalContext.current.getStringResource(R.string.tuesday),
        LocalContext.current.getStringResource(R.string.wednesday),
        LocalContext.current.getStringResource(R.string.thursday),
        LocalContext.current.getStringResource(R.string.friday),
        LocalContext.current.getStringResource(R.string.saturday)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        dayNames.forEach { dayName ->
            DayNameText(dayName)
        }
    }
}

/**
 * This is the view for the day of the week header. It displays the day of the week names.
 * @param dayName The name of the day of the week.
 */
@Composable
fun DayNameText(dayName: String) {
    Text(
        text = dayName,
        modifier = Modifier
            .padding(8.dp),
        textAlign = TextAlign.Center
    )
}