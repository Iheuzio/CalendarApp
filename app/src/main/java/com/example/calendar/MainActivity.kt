package com.example.calendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendar.ui.theme.CalendarTheme
import java.time.DayOfWeek
import java.util.Calendar
import java.util.GregorianCalendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalendarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalendarView()
                }
            }
        }
    }
}

@Composable
fun CalendarView() {
    var selectedDate by remember { mutableStateOf(GregorianCalendar()) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header with previous and next month navigation
        CalendarHeader(selectedDate) { newDate ->
            selectedDate = newDate
        }

        // Day of the week headers
        DayOfWeekHeader()

        // Calendar grid
        CalendarGrid(selectedDate) { day ->
            selectedDate = day
            // You can navigate to a new window here.
            // Implement your navigation logic.
        }
    }
}

@Composable
fun CalendarHeader(selectedDate: GregorianCalendar, onDateChange: (GregorianCalendar) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                selectedDate.add(Calendar.MONTH, -1)
                onDateChange(selectedDate)
            }
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Previous Month")
        }
        var month = ""
        when((selectedDate.get(Calendar.MONTH) + 1))
        {
            1 -> month = "January"
            2 -> month = "February"
            3 -> month = "March"
            4 -> month = "April"
            5 -> month = "May"
            6 -> month = "June"
            7 -> month = "July"
            8 -> month = "August"
            9 -> month = "September"
            10 -> month = "October"
            11 -> month = "November"
            12 -> month = "December"
        }
        BasicTextField(
            value = month,
            onValueChange = {},
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    // You can add date validation and change the selected date here.
                }
            ),
            textStyle = TextStyle(fontSize = 20.sp),
            modifier = Modifier.padding(16.dp).fillMaxWidth(0.8f)
        )

        IconButton(
            onClick = {
                selectedDate.add(Calendar.MONTH, 1)
                onDateChange(selectedDate)
            }
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "Next Month")
        }
    }
}

@Composable
fun DayOfWeekHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val dayNames = arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        for (dayName in dayNames) {
            Text(
                text = dayName,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CalendarGrid(selectedDate: GregorianCalendar, onDateClick: (GregorianCalendar) -> Unit) {
    val calendar = GregorianCalendar()
    calendar.time = selectedDate.time
    calendar.set(Calendar.DAY_OF_MONTH, 1)

    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val dayOfWeekOfFirstDay = calendar.get(Calendar.DAY_OF_WEEK) - 1

    val density = LocalDensity.current.density
    val cellSize = 40.dp
    val cellPadding = 4.dp
    val rowPadding = 4.dp

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        for (i in 0 until DayOfWeek.values().size) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = rowPadding),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (j in 0 until 7) {
                    val day = i * 7 + j + 1 - dayOfWeekOfFirstDay
                    val cellDate = GregorianCalendar()
                    cellDate.time = selectedDate.time
                    cellDate.set(Calendar.DAY_OF_MONTH, day)

                    val isCurrentMonth = day in 1..daysInMonth
                    val isSelected = calendar.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR) &&
                            calendar.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH) &&
                            calendar.get(Calendar.DAY_OF_MONTH) == selectedDate.get(Calendar.DAY_OF_MONTH)

                    Box(
                        modifier = Modifier
                            .size(cellSize)
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary
                                else if (isCurrentMonth) Color.Transparent
                                else Color.Gray
                            )
                            .clip(CircleShape)
                            .clickable {
                                if (isCurrentMonth) {
                                    onDateClick(cellDate)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCurrentMonth) {
                            Text(
                                text = day.toString(),
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onBackground,
                                fontSize = 16.sp
                            )
                        }
                    }

                    calendar.add(Calendar.DAY_OF_MONTH, 1)
                }
            }
        }
    }
}
