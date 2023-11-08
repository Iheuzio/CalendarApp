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
import java.util.Calendar
import java.util.Date
import java.text.SimpleDateFormat

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
    var selectedDate by remember { mutableStateOf(Calendar.getInstance().time) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CalendarHeader(selectedDate) { newDate ->
            selectedDate = newDate.time
        }

        DayOfWeekHeader()

        CalendarGrid(selectedDate) { day ->
            selectedDate = day.time
        }
    }
}

@Composable
fun CalendarHeader(selectedDate: Date, onDateChange: (Calendar) -> Unit) {
    val calendar = Calendar.getInstance()
    calendar.time = selectedDate

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                calendar.add(Calendar.MONTH, -1)
                onDateChange(calendar)
            }
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Previous Month")
        }

        val dateFormat = SimpleDateFormat("MMMM yyyy")
        val monthYear = dateFormat.format(calendar.time)

        BasicTextField(
            value = monthYear,
            onValueChange = {},
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                }
            ),
            textStyle = TextStyle(fontSize = 20.sp),
            modifier = Modifier.padding(16.dp).fillMaxWidth(0.8f)
        )

        IconButton(
            onClick = {
                calendar.add(Calendar.MONTH, 1)
                onDateChange(calendar)
            }
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "Next Month")
        }
    }
}

@Composable
fun DayOfWeekHeader() {
    val dayNames = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        dayNames.forEach { dayName ->
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
fun CalendarGrid(selectedDate: Date, onDateClick: (Calendar) -> Unit) {
    val calendar = Calendar.getInstance()
    calendar.time = selectedDate

    val firstDayOfMonth = calendar.clone() as Calendar
    firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1)
    val dayOfWeekOfFirstDay = firstDayOfMonth.get(Calendar.DAY_OF_WEEK) - 1

    val lastDayOfMonth = firstDayOfMonth.clone() as Calendar
    lastDayOfMonth.add(Calendar.MONTH, 1)
    lastDayOfMonth.add(Calendar.DAY_OF_MONTH, -1)
    val daysInMonth = lastDayOfMonth.get(Calendar.DAY_OF_MONTH)

    val density = LocalDensity.current.density
    val cellSize = 40.dp
    val cellPadding = 4.dp
    val rowPadding = 4.dp

    for (i in 0 until 6) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = rowPadding),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (j in 0 until 7) {
                val day = i * 7 + j + 1 - dayOfWeekOfFirstDay

                if (day in 1..daysInMonth) {
                    val cellDate = firstDayOfMonth.clone() as Calendar
                    cellDate.set(Calendar.DAY_OF_MONTH, day)

                    val isCurrentMonth = cellDate.get(Calendar.MONTH) == selectedDate.getMonth()
                    val isSelected = cellDate.time == selectedDate

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
                        Text(
                            text = day.toString(),
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onBackground,
                            fontSize = 16.sp
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier.size(cellSize),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "",
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}
