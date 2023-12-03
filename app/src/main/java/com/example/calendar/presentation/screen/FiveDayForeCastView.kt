package com.example.calendar.presentation.screen

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.calendar.R
import java.util.Locale

@Composable
fun FiveDayForecastScreen(navController: NavController) {

    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    val days = List(5) { index ->
        (calendar.clone() as Calendar).apply {
            add(Calendar.DATE, index)
        }.time
    }

    val dayFormat = SimpleDateFormat("EEEE MMMM dd", Locale.getDefault())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())


    val forecastData = List(40) {
        ForecastData(
            time = "12:00 PM",
            temperature = "${(10..30).random()}°C",
            condition = "Sunny",
            iconId = R.drawable.sunny
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Back")
        }
        LazyColumn {
            days.forEach { date ->
                item {
                    DayHeader(day = dayFormat.format(date))
                }
                items((0 until 24 step 3).toList()) { hour ->
                    val time = calendar.apply {
                        this.time = date
                        set(Calendar.HOUR_OF_DAY, hour)
                        set(Calendar.MINUTE, 0)
                    }.time
                    ForecastItem(
                        time = timeFormat.format(time),
                        temperature = "${(10..30).random()}°C",
                        condition = "Sunny",
                        iconId = R.drawable.sunny
                    )
                }
            }
        }
    }
}
@Composable
fun DayHeader(day: String) {
    Text(
        text = day,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    )

}
@Composable
fun ForecastItem(
    time: String,
    temperature: String,
    condition: String,
    iconId: Int
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = time)

        Icon(
            painter = painterResource(id = iconId),
            contentDescription = condition,
            modifier = Modifier.size(48.dp)
        )

        Text(text = temperature)

        Text(text = condition)
    }
}
fun generateForecastTimes(calendar: Calendar): List<String> {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return List(8) { index ->
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, index * 3)
            set(Calendar.MINUTE, 0)
        }
        timeFormat.format(calendar.time)
    }
}
// Placeholder data
data class ForecastData(
    val time: String,
    val temperature: String,
    val condition: String,
    val iconId: Int
)
