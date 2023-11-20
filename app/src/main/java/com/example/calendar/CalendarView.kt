package com.example.calendar

import android.app.usage.UsageEvents
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CalendarView(viewModel: EventViewModel, navController: NavController) {
    var selectedDate by remember { mutableStateOf(Calendar.getInstance().time) }
    var showDailyOverview by remember { mutableStateOf(false) }

    // temporary placeholder for events
    val events = remember { mutableStateListOf<UsageEvents.Event>() }

    if (!showDailyOverview) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CalendarHeader(selectedDate) { newDate ->
                selectedDate = newDate.time
            }

            DayOfWeekHeader()

            CalendarGrid(selectedDate) { day ->
                selectedDate = day.time
                //after day is clicked, show day overview
                showDailyOverview = true
            }

            Button(
                onClick = {
                    viewModel.selectedEvent = null
                    navController.navigate(NavRoutes.CreateEvent.route)
                }
            ) {
                Text("Create event")
            }

        }
    } else {
        val format = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
        val date = format.format(selectedDate)
        navController.navigate(NavRoutes.DayView.route + "/$date")
    }
}

@Composable
fun MonthView(navController: NavController, calendarModel: CalendarViewModel) {
    val selectedDate = calendarModel.selectedDate.value

    Column(modifier = Modifier.fillMaxSize()) {
        CalendarHeader(selectedDate) { newDate ->
            calendarModel.onDateChange(newDate)
        }

        DayOfWeekHeader()

        CalendarGrid(selectedDate) { day ->
            calendarModel.onDateChange(day)
        }

        Button(
            onClick = {
                navController.navigate(NavRoutes.CreateEvent.route)
            }
        ) {
            Text(LocalContext.current.getStringResource(R.string.add_event))
        }
    }
}

@Composable
fun CalendarView(navController: NavController, calendarModel: CalendarViewModel, eventModel: EventViewModel) {
    val showDailyOverview = calendarModel.showDailyOverview.value

    if (!showDailyOverview) {
        MonthView(navController, calendarModel)
    } else {

        val selectedDate = calendarModel.selectedDate.value
        val format = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
        val date = format.format(selectedDate)
        //navController.navigate(NavRoutes.DayView.route + "/$date")
        DailyOverview(navController, calendarModel, eventModel)
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
            Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = LocalContext.current.getStringResource(R.string.previous_month))
        }

        monthYearHeader(calendar)

        IconButton(
            onClick = {
                calendar.add(Calendar.MONTH, 1)
                onDateChange(calendar)
            }
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = LocalContext.current.getStringResource(R.string.next_month))
        }
    }
}

@Composable
fun CalendarGrid(selectedDate: Date, onDateClick: (Calendar) -> Unit) {
    //loop over list of events for this given month to make them appear

    val calendar = Calendar.getInstance()
    calendar.time = selectedDate

    val firstDayOfMonth = calendar.clone() as Calendar
    firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1)
    val dayOfWeekOfFirstDay = firstDayOfMonth.get(Calendar.DAY_OF_WEEK) - 1

    val lastDayOfMonth = firstDayOfMonth.clone() as Calendar
    lastDayOfMonth.add(Calendar.MONTH, 1)
    lastDayOfMonth.add(Calendar.DAY_OF_MONTH, -1)
    val daysInMonth = lastDayOfMonth.get(Calendar.DAY_OF_MONTH)

    val cellSize = 40.dp
    val rowPadding = 4.dp

    for (i in 0 until 6) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = rowPadding),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (j in 0 until 7) {
                val day = i * 7 + j + 1 - dayOfWeekOfFirstDay

                if (day in 1..daysInMonth) {
                    val cellDate = firstDayOfMonth.clone() as Calendar
                    cellDate.set(Calendar.DAY_OF_MONTH, day)

                    val isCurrentMonth = cellDate.get(Calendar.MONTH) == selectedDate.month
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
