package com.example.calendar.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavController
import com.example.calendar.data.database.Event
import com.example.calendar.R
import com.example.calendar.data.NavRoutes
import com.example.calendar.presentation.viewmodels.CalendarViewModel
import com.example.calendar.presentation.viewmodels.EventViewModel
import com.example.calendar.presentation.getStringResource
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.calendar.presentation.viewmodels.HolidayViewModel


@Composable
fun DailyOverviewScreen(
    navController: NavController,
    selectedDate: Date,
    events: List<Event>,
    onEventSelected: (Event?) -> Unit,
    onAddEvent: () -> Unit,
    onChangeDate: (Date) -> Unit,
    onBack: () -> Unit,
    onEditEvent: (Event) -> Unit,
    holidayModel: HolidayViewModel
) {
    //dailyViewModel.eventsForSelectedDate.observeAsState(listOf())
    val today = Calendar.getInstance().time


    Column(modifier = Modifier.fillMaxSize()) {
        Row {
            Button(
                onClick = onBack,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(LocalContext.current.getStringResource(R.string.back))
            }
            Button(
                onClick = onAddEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                content = {
                    Text(LocalContext.current.getStringResource(R.string.create_event))
                }
            )
        }
        //only display weather if it is today's date that is selected
        if (selectedDate.isSameDayAs(today)) {
            WeatherDisplay(navController)
        }
        DailyHeader(selectedDate, onChangeDate)

        HolidayDisplay(holidayModel, selectedDate)

        DailyEventsList(selectedDate = selectedDate, events = events, onEventSelected, onEditEvent)
    }
}
    @Composable
    fun DailyHeader(selectedDate: Date, onChangeDate: (Date) -> Unit) {
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
                    contentDescription = LocalContext.current.getStringResource(R.string.back)
                )
            }

            Text(text = dateString)

            Button(onClick = {
                onChangeDate(getNextDay(selectedDate))
            }) {
                Image(
                    painter = painterResource(id = R.drawable.arrow_forward),
                    contentDescription = LocalContext.current.getStringResource(R.string.next)
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
fun EventItem(event: Event, onEventSelected: (Event?) -> Unit, onEditEvent: (Event) -> Unit) {
    Row(
        modifier = Modifier
            .testTag("EventItem-${event.id}")
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .height(80.dp)
                .background(Color(0xFFE1BEE7), RoundedCornerShape(4.dp))
                .background(
                    Color(0xFFE1BEE7),
                    RoundedCornerShape(4.dp)
                ) // A light purple color and rounded corners
                .padding(8.dp)
        ) {
            Text(text = event.title)
            Text(text = event.description)
            Text(text = "${event.startTime} - ${event.endTime}")
        }

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Button(
                onClick = {
                    onEventSelected(event)
                },
                modifier = Modifier
                    .size(100.dp, 40.dp)
            ) {
                Text(stringResource(R.string.details))
            }
            Button(
                onClick = {
                    onEditEvent(event)
                },
                modifier = Modifier
                    .size(100.dp, 40.dp)
            ) {
                Text(stringResource(R.string.edit))
            }
        }
    }
}

@Composable
fun DailyEventsList(selectedDate: Date, events: List<Event>, onEventSelected: (Event?) -> Unit, onEditEvent: (Event) -> Unit) {
    val hoursOfDay = (0..23).toList()
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val sortedEvents = events.sortedBy { timeFormatter.parse(it.startTime)?.time }

    LazyColumn {
        items(hoursOfDay) { hour ->
            // Format hour to "HH:mm"
            val hourStartString = String.format(Locale.getDefault(), "%02d:00", hour)
            val hourEndString = String.format(Locale.getDefault(), "%02d:00", hour + 1)

            // Check if this hour is within any events
            val eventsThisHour = remember { mutableStateListOf<Event>() }
            LaunchedEffect(key1 = selectedDate) {
                eventsThisHour.addAll(
                    sortedEvents.filter { event ->
                        val eventStart = timeFormatter.parse(event.startTime)
                        val eventEnd = timeFormatter.parse(event.endTime)
                        val hourStart = timeFormatter.parse(hourStartString)
                        val hourEnd = timeFormatter.parse(hourEndString)

                        // Event covers this hour if it starts before hourEnd and ends after hourStart
                        eventStart.before(hourEnd) && eventEnd.after(hourStart)
                })
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 8.dp)
            ) {
                //display hour
                Column(
                    modifier = Modifier
                        .width(80.dp)
                        .padding(end = 8.dp)
                ) {
                    Text(text = hourStartString)
                }
                //display event if any
                Column(modifier = Modifier.weight(1f)) {
                    if (eventsThisHour.isNotEmpty()) {
                        val event = eventsThisHour.first()
                        EventItem(event, onEventSelected, onEditEvent)
                    } else {
                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                        )
                    }
                }


                if (eventsThisHour.isNotEmpty()) {
                    val event = eventsThisHour.first()
                    EventItem(event, onEventSelected, onEditEvent)
                } else {
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun DailyOverview(navController: NavController, calendarModel: CalendarViewModel, eventModel: EventViewModel, holidayModel: HolidayViewModel) {
    val selectedDate = calendarModel.selectedDate.value
    val events = eventModel.events

    DailyOverviewScreen(
        navController = navController,
        selectedDate = selectedDate,
        events = events,
        onEventSelected = { event ->
            // handle event selected action
            eventModel.selectedEvent = event
            navController.navigate(NavRoutes.EventView.route)
        },
        onAddEvent = {
            navController.navigate(NavRoutes.CreateEvent.route)
        },
        onChangeDate = { newDate ->
            val calendar = Calendar.getInstance()
            calendar.time = newDate
            calendarModel.onDateChange(calendar)
        },
        onBack = {
            calendarModel.toggleShowDailyOverview()
        },
        onEditEvent = { event ->
            eventModel.selectedEvent = event
            navController.navigate(NavRoutes.EditEvent.route)

        },
        holidayModel
    )
}

@Composable
fun HolidayDisplay(holidayModel: HolidayViewModel, selectedDate: Date) {
    val format = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())
    val date = format.format(selectedDate)
    val holidaysToday = holidayModel.holidays.filter { holiday ->
        holiday.date == date
    }
    for (holiday in holidaysToday) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()) {
            Text(holiday.name, fontWeight = FontWeight.Bold)
            if (holiday.location.isNotEmpty() && holiday.location != "null") {
                Row {
                    Text(LocalContext.current.getStringResource(R.string.celebrated_in))
                    Text(holiday.location)
                }
            }
        }
    }
}

//helper function to check if days are equal
fun Date.isSameDayAs(otherDate: Date): Boolean {
    val calendar1 = Calendar.getInstance().apply { time = this@isSameDayAs }
    val calendar2 = Calendar.getInstance().apply { time = otherDate }
    return calendar1.get(Calendar.ERA) == calendar2.get(Calendar.ERA) &&
            calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
            calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
}

