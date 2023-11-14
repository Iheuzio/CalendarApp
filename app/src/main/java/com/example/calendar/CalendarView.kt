package com.example.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
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
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import java.util.*

@Composable
fun CalendarView(viewModel: EventViewModel, navController: NavController) {
    var selectedDate by remember { mutableStateOf(Calendar.getInstance().time) }
    val isEventCreationDialogVisible = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CalendarHeader(selectedDate) { newDate ->
            selectedDate = newDate.time
        }

        DayOfWeekHeader()

        CalendarGrid(viewModel, selectedDate) { day ->
            selectedDate = day.time
        }

        //EventCreationButton(isEventCreationDialogVisible)
        Button(
            onClick = {
                navController.navigate(NavRoutes.CreateEditEvent.route)
            }
        ) {
            Text("Create event")
        }


        if (isEventCreationDialogVisible.value) {
            EventCreationDialog(onDismiss = { isEventCreationDialogVisible.value = false })
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
            Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = LocalContext.current.getStringResource(R.string.previous_month))
        }

        monthYearHeader()

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
fun CalendarGrid(viewModel: EventViewModel, selectedDate: Date, onDateClick: (Calendar) -> Unit) {
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
                        val event = viewModel.findItem(day.toString() + "/" + cellDate.get(Calendar.MONTH).toString()
                                + "/" + cellDate.get(Calendar.YEAR).toString())
                        if (event != null) {
                            Text(
                                viewModel.selectedEvent.title
                            )
                        }
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
