package com.example.calendar.presentation.screen

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.calendar.R
import com.example.calendar.data.NavRoutes
import com.example.calendar.data.database.AppDatabase
import com.example.calendar.data.database.Event
import com.example.calendar.presentation.viewmodels.CalendarViewModel
import com.example.calendar.presentation.viewmodels.EventViewModel
import com.example.calendar.presentation.getStringResource
import com.example.calendar.presentation.viewmodels.DailyViewModel
import java.util.*

/**
 * This is the main view of the calendar. It switches between the MonthView and the DailyOverview based on the value of showDailyOverview in the CalendarViewModel.
 *
 * @param navController The NavController used for navigation.
 * @param calendarModel The ViewModel that holds the state of the calendar.
 * @param eventModel The ViewModel that holds the state of the events.
 */
@Composable
fun CalendarView(navController: NavController, calendarModel: CalendarViewModel, eventModel: EventViewModel, dayModel: DailyViewModel, database: AppDatabase) {
    val showDailyOverview = calendarModel.showDailyOverview.value

    if (!showDailyOverview) {
        MonthView(navController, calendarModel, eventModel, database)
    } else {
        DailyOverview(navController, calendarModel, dayModel, eventModel, database)
    }
}

/**
 * This is the view for the month. It displays the month header, the day of the week header, the calendar grid, and the add event button.
 * @param navController The NavController used for navigation.
 * @param calendarModel The ViewModel that holds the state of the calendar.
 * @param eventModel The ViewModel that holds the state of the events.
 */
@Composable
fun MonthView(navController: NavController, calendarModel: CalendarViewModel, eventModel: EventViewModel, database: AppDatabase) {
    val selectedDate = calendarModel.selectedDate.value
    Column(modifier = Modifier.fillMaxSize()) {
        CalendarHeader(selectedDate, calendarModel::onDateChange)
        DayOfWeekHeader()
        CalendarGrid(eventModel, selectedDate, calendarModel::onDateChange, database)
        AddEventButton(navController)
    }
}

/**
 * This is the header of the calendar. It displays the month and year and has buttons to change the month.
 *
 * @param selectedDate The currently selected date.
 * @param onDateChange The function to call when the date is changed.
 */
@Composable
fun CalendarHeader(selectedDate: Date, onDateChange: (Calendar) -> Unit) {
    val calendar = Calendar.getInstance().apply { time = selectedDate }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MonthChangeButton(calendar, onDateChange, Icons.Default.KeyboardArrowLeft, -1, R.string.previous_month)
        monthYearHeader(calendar)
        MonthChangeButton(calendar, onDateChange, Icons.Default.KeyboardArrowRight, 1, R.string.next_month)
    }
}

/**
 * This is a button that changes the month. It displays an icon and changes the month when clicked.
 *
 * @param calendar The calendar to change the month of.
 * @param onDateChange The function to call when the date is changed.
 * @param icon The icon to display on the button.
 * @param monthChange The amount to change the month by.
 * @param descriptionId The resource id of the content description for the icon.
 */
@Composable
fun MonthChangeButton(calendar: Calendar, onDateChange: (Calendar) -> Unit, icon: ImageVector, monthChange: Int, descriptionId: Int) {
    IconButton(
        onClick = {
            calendar.add(Calendar.MONTH, monthChange)
            onDateChange(calendar)
        }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = LocalContext.current.getStringResource(descriptionId)
        )
    }
}

/**
 * This is a button that navigates to the create event screen when clicked.
 *
 * @param navController The NavController used for navigation.
 */
@Composable
fun AddEventButton(navController: NavController) {
    Button(
        onClick = {
            navController.navigate(NavRoutes.CreateEvent.route)
        }
    ) {
        Text(LocalContext.current.getStringResource(R.string.add_event))
    }
}

/**
 * This is the grid of the calendar. It displays the days of the month and highlights the selected day and today.
 *
 * @param eventModel The ViewModel that holds the state of the events.
 * @param selectedDate The currently selected date.
 * @param onDateClick The function to call when a date is clicked.
 */
@Composable
fun CalendarGrid(eventModel: EventViewModel, selectedDate: Date, onDateClick: (Calendar) -> Unit, database: AppDatabase) {
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
                    // check if events exist for this day
                    val eventBool = eventModel.checkEventsExist(cellDate.time)
                    DayCell(day, isSelected, isCurrentMonth, cellSize, onDateClick, cellDate, eventBool)
                } else {
                    EmptyCell(cellSize)
                }
            }
        }
    }
}

/**
 * This is a cell in the calendar grid. It displays the day of the month and highlights the cell if it is the selected day, today, or if there are events on this day.
 *
 * @param day The day of the month.
 * @param isSelected Whether this day is the selected day.
 * @param isCurrentMonth Whether this day is in the current month.
 * @param cellSize The size of the cell.
 * @param onDateClick The function to call when a date is clicked.
 * @param cellDate The date of this cell.
 * @param eventBool Whether there are events on this day.
 */
@Composable
fun DayCell(
    day: Int,
    isSelected: Boolean,
    isCurrentMonth: Boolean,
    cellSize: Dp,
    onDateClick: (Calendar) -> Unit,
    cellDate: Calendar,
    eventBool: Any
) {
    val today = Calendar.getInstance()
    val isToday = cellDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            cellDate.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
            cellDate.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)

    Box(
        modifier = Modifier
            .size(cellSize)
            .background(
                when {
                    isSelected -> MaterialTheme.colorScheme.primary
                    isToday -> Color.Black
                    eventBool == true -> Color.Red
                    isCurrentMonth -> Color.Transparent
                    else -> Color.Gray
                }
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
            color = if (isSelected || isToday) Color.White else MaterialTheme.colorScheme.onBackground,
            fontSize = 16.sp
        )
    }
}

/**
 * This is an empty cell in the calendar grid. It is used for days that are not in the current month.
 *
 * @param cellSize The size of the cell.
 */
@Composable
fun EmptyCell(cellSize: Dp) {
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