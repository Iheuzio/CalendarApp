package com.example.calendar

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

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.*

@Composable
fun MonthView(navController: NavController, calendarModel: CalendarViewModel) {
    val selectedDate = calendarModel.selectedDate.value

    Column(modifier = Modifier.fillMaxSize()) {
        CalendarHeader(selectedDate) { newDate ->
            calendarModel.onDateChange(newDate, false)
        }

        DayOfWeekHeader()

        CalendarGrid(selectedDate) { day ->
            if (calendarModel.isEventCreationDialogVisible.value) {
                calendarModel.onDateChange(day, true)
            }
            calendarModel.onDateChange(day, false)
            calendarModel.isEventCreationDialogVisible.value = true
        }

        Button(
            onClick = {
                navController.navigate(NavRoutes.CreateEditEvent.route)
            }
        ) {
            Text("Create event")
        }
    }
}

@Composable
fun DailyOverview(navController: NavController, calendarModel: CalendarViewModel) {
    val selectedDate = calendarModel.selectedDate.value
    val events = calendarModel.events.value
    val placeholderEvents = listOf(
        Event(
            id = "1",
            title = "Meeting",
            description = "Team meeting",
            date = "2023-11-14",
            time = "10:00",
            startTime = "10:00",
            endTime = "11:00"
        ),
        Event(
            id = "2",
            title = "Doctor Appointment",
            description = "Routine check-up",
            date = "2023-11-14",
            time = "12:00",
            startTime = "12:00",
            endTime = "13:00"
        ),
        Event(
            id = "3",
            title = "Lunch with Friends",
            description = "Catch up lunch",
            date = "2023-11-14",
            time = "14:00",
            startTime = "14:00",
            endTime = "15:30"
        )
    )
    calendarModel.events.value = placeholderEvents
    DailyOverviewScreen(
        selectedDate = selectedDate,
        events = calendarModel.events.value,
        onEventSelected = { event ->
            // handle event selected action
        },
        onAddEvent = {
            // handle add event action
        },
        onChangeDate = { newDate ->
            val calendar = Calendar.getInstance()
            calendar.time = newDate
            calendarModel.onDateChange(calendar, true)
        },
        onNavigateToCreateEvent = {
            navController.navigate(NavRoutes.CreateEditEvent.route)
        },
        onBack = {
            calendarModel.toggleShowDailyOverview()
        }
    )
}

@Composable
fun CalendarView(navController: NavController, calendarModel: CalendarViewModel) {
    val showDailyOverview = calendarModel.showDailyOverview.value

    if (!showDailyOverview) {
        MonthView(navController, calendarModel)
    } else {
        DailyOverview(navController, calendarModel)
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
