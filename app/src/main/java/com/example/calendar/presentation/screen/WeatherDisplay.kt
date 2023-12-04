package com.example.calendar.presentation.screen

import WeatherResponse
import android.text.TextUtils.replace
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.calendar.R
import com.example.calendar.data.NavRoutes
import com.example.calendar.utils.RetrofitInstance
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
fun WeatherDisplay(
    navController: NavController,
    temperature: String = "21°C",
    weatherCondition: String = "Sunny",
    lastUpdated: String = "10:00 AM"
) {
    var weatherData by remember { mutableStateOf<WeatherResponse?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val apiKey = "ERtoam8JXYf21rCXIfEhd9w1gZVhLkU6"
    val locationKey = "349727"
    val imageName =

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val response = RetrofitInstance.api.getDailyForecast(locationKey, apiKey)
                if (response.isSuccessful) {
                    weatherData = response.body()
                    Log.d("data", weatherData.toString())
                } else {
                    Log.e("WeatherDisplay", "Error fetching weather data: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("WeatherDisplay", "Exception fetching weather data", e)
            }
        }
    }

    // add refetch data every 10 min later
    LaunchedEffect(weatherData) {
        delay(600_000) // 10 min
        coroutineScope.launch {
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
            .clickable(onClick = {
                navController.navigate(NavRoutes.FiveDayForecast.route)
            })
    ) {
        weatherData?.let { data ->
            val forecast = data.DailyForecasts.first()
            val tempCelsius = ((forecast.Temperature.Minimum.Value - 32) * 5/9).toInt()
            val condition = forecast.Day.IconPhrase
            val iconId = getDrawableResourceForCondition(condition)


            val date = forecast.Date
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = weatherCondition,
                modifier = Modifier.size(48.dp)
            )

            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(text = "Temperature: $tempCelsius°C")
                Text(text = "Condition: $condition")
                Text(text = "Last updated: $date")
        } ?: Text(text = "Fetching weather...")

        }
    }
}

@DrawableRes
fun getDrawableResourceForCondition(condition: String): Int {
    return when (condition) {
        "sunny" -> R.drawable.sunny
        "cloudy" -> R.drawable.cloudy
        "Intermittent clouds" -> R.drawable.intermittentclouds
        else -> R.drawable.unknown_weather_condition
    }
}
