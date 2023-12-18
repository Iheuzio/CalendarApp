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
import com.example.calendar.utils.OpenWeatherMapForecastResponse
import com.example.calendar.utils.RetrofitInstance
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun FiveDayForecastScreen(navController: NavController, latitude: Double, longitude: Double) {
    var weatherData by remember { mutableStateOf<OpenWeatherMapForecastResponse?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val apiKey = "012a14aa06d38dc98fca31414f0d7bf2"

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val response = RetrofitInstance.api.getFiveDayForecast(latitude, longitude, apiKey)
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

        LazyColumn {
            weatherData?.list?.groupBy { it.dt_txt.substring(0, 10) }?.forEach { (date, forecasts) ->
                item {
                    DayHeader(date)
                }
                items(forecasts) { forecast ->
                    ForecastItem(
                        time = forecast.dt_txt.substring(11, 16),
                        temperature = "${forecast.main.temp - 273.15}°C", // Kelvin to Celsius
                        condition = forecast.weather.first().description,
                        iconId = getDrawableResourceForCondition(forecast.weather.first().main)
                    )
                }
            }
        }
    }
}
@Composable
fun DayHeader(day: String) {
    val parsedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(day)
    val formattedDate = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(parsedDate)
    Text(
        text = formattedDate,
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
fun formatTime(hour: Int): String {
    return String.format("%02d:00", hour)
}

