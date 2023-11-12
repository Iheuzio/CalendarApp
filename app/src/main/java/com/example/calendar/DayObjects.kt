package com.example.calendar

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
import java.text.SimpleDateFormat
import java.util.Calendar

@Composable
fun monthYearHeader() {
    val calendar = Calendar.getInstance()
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

@Composable
fun DayNameText(dayName: String) {
    Text(
        text = dayName,
        modifier = Modifier
            .padding(8.dp),
        textAlign = TextAlign.Center
    )
}