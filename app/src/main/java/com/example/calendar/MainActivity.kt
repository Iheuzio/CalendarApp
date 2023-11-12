package com.example.calendar

import android.content.Context
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendar.ui.theme.CalendarTheme
import java.text.SimpleDateFormat
import java.util.*
import androidx.annotation.StringRes
import androidx.compose.ui.platform.LocalContext


fun Context.getStringResource(@StringRes resId: Int): String {
    return resources.getString(resId)
}

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
    val isEventCreationDialogVisible = remember { mutableStateOf(false) }

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

        EventCreationButton(isEventCreationDialogVisible)

        if (isEventCreationDialogVisible.value) {
            EventCreationDialog(onDismiss = { isEventCreationDialogVisible.value = false })
        }
    }
}

// Replace with your compose nav function
@Composable
fun EventCreationButton(isEventCreationDialogVisible: MutableState<Boolean>) {
    FloatingActionButton(
        onClick = { isEventCreationDialogVisible.value = true },
        modifier = Modifier
            .padding(16.dp)
            .size(56.dp),
        content = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = LocalContext.current.getStringResource(R.string.create_event),
                tint = Color.White
            )
        }
    )
}

// This should be swapped for a nav composable rather than a popup dialog
@Composable
fun EventCreationDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(LocalContext.current.getStringResource(R.string.create_event)) },
        text = {
            var eventName by remember { mutableStateOf("") }

            BasicTextField(
                value = eventName,
                onValueChange = { eventName = it },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {

                    }
                )
            )
        },
        confirmButton = {
            ConfirmButton(onDismiss)
        },
        dismissButton = {
            DismissButton(onDismiss)
        }
    )
}

@Composable
fun ConfirmButton(onConfirm: () -> Unit) {
    TextButton(
        onClick = onConfirm
    ) {
        Text(LocalContext.current.getStringResource(R.string.confirm))
    }
}

@Composable
fun DismissButton(onDismiss: () -> Unit) {
    TextButton(
        onClick = onDismiss
    ) {
        Text(LocalContext.current.getStringResource(R.string.cancel))
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
