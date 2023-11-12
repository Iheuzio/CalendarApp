package com.example.calendar

import android.app.usage.UsageEvents
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.lazy.items


    @Composable
    fun DailyOverviewScreen(selectedDate: Date, events: List<UsageEvents.Event>, onEventSelected: (UsageEvents.Event) -> Unit, onAddEvent: () -> Unit, onChangeDate: (Date) -> Unit) {
        Column(modifier = Modifier.fillMaxSize()) {
            //temp events list:
            val events = listOf(
                Event(id = 1, title = "evt 1", description = "description", time = "9:00 AM"),
                Event(id = 2, title = "evt2", description = "description", time = "12:00 PM"),
                Event(id = 3, title = "event 3", description = "description", time = "2:00 PM")
            )

            DailyHeader(selectedDate, onChangeDate)
            DailyEventsList(events = events)

        }
    }
    @Composable
    fun DailyHeader(selectedDate: Date, onChangeDate: (Date) -> Unit) {
        val context = LocalContext.current
        val dateFormat = remember { SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault()) }
        val dateString = dateFormat.format(selectedDate)

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                onChangeDate(getPreviousDay(selectedDate))
            }) {
                Image(
                    painter = painterResource(id = R.drawable.arrow_back),
                    contentDescription = "Back"
                )
            }

            Text(text = dateString)

            Button(onClick = {
                onChangeDate(getNextDay(selectedDate))
            }) {
                Image(
                    painter = painterResource(id = R.drawable.arrow_forward),
                    contentDescription = "Next"
                )
            }
        }
    }

    private fun getPreviousDay(selectedDate: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = selectedDate
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        return calendar.time
    }

private fun getNextDay(selectedDate: Date): Date {
    val calendar = Calendar.getInstance()
    calendar.time = selectedDate
    calendar.add(Calendar.DAY_OF_MONTH, 1)
    return calendar.time
}
@Composable
fun EventItem(event: Event) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Text(
            text = event.time,
            modifier = Modifier.width(80.dp)
        )
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = event.title
            )
        }
    }
    Divider()
}
@Composable
fun DailyEventsList(events: List<Event>) {
    val hoursOfDay = (8..24).toList()  //8- midnihgt
    val timeFormatter = remember { SimpleDateFormat("h:mm a", Locale.getDefault()) }

    //events should be sorted by time later
    LazyColumn {
        items(hoursOfDay) { hour ->
            val eventsAtThisHour = events.filter {
                val eventTime = timeFormatter.parse(it.time)
                val eventCalendar = Calendar.getInstance().apply { time = eventTime }
                eventCalendar.get(Calendar.HOUR_OF_DAY) == hour
            }
            if (eventsAtThisHour.isNotEmpty()) {
                //if theres an evt, display it
                eventsAtThisHour.forEach { event ->
                    EventItem(event)
                }
            } else {
                // otherwise just display empty hour
                TimeSlot(hour)
            }
        }
    }
}
@Composable
fun TimeSlot(hour: Int) {
    val timeString = remember(hour) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, 0)
        }
        SimpleDateFormat("h:mm a", Locale.getDefault()).format(calendar.time)
    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Text(
            text = timeString,
            modifier = Modifier.width(80.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
    }
    Divider()
}
