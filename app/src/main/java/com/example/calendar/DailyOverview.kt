package com.example.calendar

import android.app.usage.UsageEvents
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

    @Composable
    fun DailyOverviewScreen(selectedDate: Date, events: List<UsageEvents.Event>, onEventSelected: (UsageEvents.Event) -> Unit, onAddEvent: () -> Unit, onChangeDate: (Date) -> Unit) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Step 1: Display the header with the selected day and navigation buttons
            DailyHeader(selectedDate, onChangeDate)

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
                //onChangeDate(getPreviousDay(selectedDate))
            }) {
                Text(text = "Previous Day")
            }

            Text(text = dateString)

            Button(onClick = {
                //onChangeDate(getNextDay(selectedDate))
            }) {
                Text(text = "Next Day")
            }
        }
    }

//    private fun getPreviousDay(selectedDate: Date): Date {
//
//    }
//
//    private fun getNextDay(selectedDate: Date): Date {
//
//    }
