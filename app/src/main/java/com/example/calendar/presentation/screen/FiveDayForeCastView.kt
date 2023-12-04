package com.example.calendar.presentation.screen

import WeatherResponse
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.calendar.R
import com.example.calendar.utils.RetrofitInstance
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun FiveDayForecastScreen(navController: NavController) {
    var weatherData by remember { mutableStateOf<WeatherResponse?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val apiKey = "ERtoam8JXYf21rCXIfEhd9w1gZVhLkU6"
    val locationKey = "56186"

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val response = RetrofitInstance.api.getFiveDayForecast(locationKey, apiKey)
                if (response.isSuccessful) {
                    weatherData = response.body()
                    Log.d("5daydata", weatherData.toString())
                } else {
                    Log.e("FiveDayForecast", "Error fetching weather data: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("FiveDayForecast", "Exception in fetching weather data", e)
            }
        }
    }

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




    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Back")
        }
//        LazyColumn {
//            days.forEach { date ->
//                item {
//                    DayHeader(day = dayFormat.format(date))
//                }
//                items((0 until 24 step 3).toList()) { hour ->
//                    val time = calendar.apply {
//                        this.time = date
//                        set(Calendar.HOUR_OF_DAY, hour)
//                        set(Calendar.MINUTE, 0)
//                    }.time
//                    ForecastItem(
//                        time = timeFormat.format(time),
//                        temperature = "${(10..30).random()}°C",
//                        condition = "Sunny",
//                        iconId = R.drawable.sunny
//                    )
//                }
//            }
//        }
        LazyColumn {
            weatherData?.DailyForecasts?.forEach { dailyForecast ->
                item {
                    val parsedDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault()).parse(dailyForecast.Date)
                    val formattedDate = SimpleDateFormat("EEEE, MMMM dd", Locale.getDefault()).format(parsedDate)

                    Text(text = formattedDate, modifier = Modifier.padding(16.dp).fillMaxWidth())
                }
                item {
                    // Convert temp
                    val minTemp = dailyForecast.Temperature.Minimum.Value
                    val maxTemp = dailyForecast.Temperature.Maximum.Value
                    val avgTempCelsius = ((minTemp + maxTemp) / 2 - 32) * 5 / 9
                    val condition = dailyForecast.Day.IconPhrase

                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Avg Temp: ${avgTempCelsius.toInt()}°C")
                        Text(text = "Condition: $condition")
                        Icon(
                            painter = painterResource(id = getDrawableResourceForCondition(condition)),
                            contentDescription = condition,
                            modifier = Modifier.size(48.dp)
                        )
                    }
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
