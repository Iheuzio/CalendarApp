package com.example.calendar.presentation

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.calendar.R
import com.example.calendar.data.NavRoutes
import com.example.calendar.data.viewmodels.CalendarViewModel
import com.example.calendar.data.viewmodels.EventViewModel
import java.text.SimpleDateFormat
import java.util.*

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
        PreviousMonthButton(calendar = calendar, onDateChange = onDateChange)

        monthYearHeader(calendar)
        NextMonthButton(calendar = calendar, onDateChange = onDateChange)
    }
}

@Composable
fun PreviousMonthButton(calendar: Calendar, onDateChange: (Calendar) -> Unit) {
    IconButton(
        onClick = {
            calendar.add(Calendar.MONTH, -1)
            onDateChange(calendar)
        }
    ) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowLeft,
            contentDescription = LocalContext.current.getStringResource(R.string.previous_month)
        )
    }
}


@Composable
fun NextMonthButton(calendar: Calendar, onDateChange: (Calendar) -> Unit) {
    IconButton(
        onClick = {
            calendar.add(Calendar.MONTH, 1)
            onDateChange(calendar)
        }
    ) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = LocalContext.current.getStringResource(R.string.next_month)
        )
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

                    DayCell(day, isSelected, isCurrentMonth, cellSize, onDateClick, cellDate)
                } else {
                    EmptyCell(cellSize)
                }
            }
        }
    }
}

@Composable
fun DayCell(day: Int, isSelected: Boolean, isCurrentMonth: Boolean, cellSize: Dp, onDateClick: (Calendar) -> Unit, cellDate: Calendar) {
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