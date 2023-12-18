package com.example.calendar.presentation.screen

import OpenWeatherMapCurrentWeatherResponse
import WeatherResponse
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.text.TextUtils.replace
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.calendar.R
import com.example.calendar.data.NavRoutes
import com.example.calendar.utils.LocationUtil
import com.example.calendar.utils.OpenWeatherMapForecastResponse
import com.example.calendar.utils.RetrofitInstance
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherDisplay(
    navController: NavController
) {
    var weatherData by remember { mutableStateOf<OpenWeatherMapCurrentWeatherResponse?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val apiKey = "012a14aa06d38dc98fca31414f0d7bf2"
    var location by remember { mutableStateOf<Location?>(null) }
    val context = LocalContext.current

    // Fetching the location
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val locationUtil = LocationUtil(context)
            locationUtil.startLocationUpdates(onSuccess = { newLocation ->
                location = newLocation
            }, onFailure = { error ->
                Log.e("WeatherDisplay", error.toString())
            })
        }
    }

    // Fetching the weather data
    LaunchedEffect(location) {
        location?.let {
            coroutineScope.launch {
                try {
                    val response = RetrofitInstance.api.getCurrentWeather(
                        latitude = it.latitude,
                        longitude = it.longitude,
                        apiKey = apiKey
                    )
                    if (response.isSuccessful) {
                        weatherData = response.body()
                        Log.d("WeatherData", weatherData.toString())
                    } else {
                        Log.e("WeatherDisplay", "Error fetching weather data: ${response.errorBody()?.string()}")
                    }
                } catch (e: Exception) {
                    Log.e("WeatherDisplay", "Exception fetching weather data", e)
                }
            }
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
            .clickable {     navController.navigate("fiveDayForecast/${location?.latitude}/${location?.longitude}")
            }
    ) {
        weatherData?.let { data ->
            val tempCelsius = data.main.temp - 273.15 // kelvin to Celsius
            val condition = data.weather.first().description
            Log.d("condition", condition)
            val iconId = getDrawableResourceForCondition(condition)
            Log.d("condition", iconId.toString())
            val lastUpdated = Instant.ofEpochSecond(data.dt).atZone(ZoneId.systemDefault()).toLocalDateTime()

            Icon(
                painter = painterResource(id = iconId),
                contentDescription = condition,
                modifier = Modifier.size(48.dp)
            )

            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(text = "Temperature: ${tempCelsius.toInt()}°C")
                Text(text = "Condition: $condition")
                Text(text = "Last updated: ${lastUpdated.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))}")
            }
        } ?: Text(text = "Fetching weather...")
    }
}


@DrawableRes
fun getDrawableResourceForCondition(condition: String): Int {
    return when (condition) {
        "Sunny", "Clear" -> R.drawable.sunny
        "Mostly sunny", "Mostly clear" -> R.drawable.sunny
        "Partly sunny", "haze", "few clouds" -> R.drawable.intermittentclouds
        "Hazy sunshine", "Hazy moonlight" -> R.drawable.sunny
        "Clouds", "Mostly cloudy", "overcast clouds", "broken clouds", "scattered clouds", "few clouds" -> R.drawable.cloudy
        "Dreary" -> R.drawable.cloudy
        "Fog" -> R.drawable.cloudy
        "Showers", "Rain", "Partly Sunny w/ Showers", "Mostly Cloudy w/ Showers" -> R.drawable.rainy
        "T-Storms", "Mostly Cloudy w/ T-Storms", "Partly Sunny w/ T-Storms" -> R.drawable.cloudy
        "Snow", "Flurries", "Mostly Cloudy w/ Flurries", "Partly Sunny w/ Flurries", "Mostly Cloudy w/ Snow" -> R.drawable.snowy
        "Ice", "Sleet", "Freezing rain", "Rain and snow" -> R.drawable.grain
        "Hot" -> R.drawable.sunny
        "Cold" -> R.drawable.snowy
        "Windy" -> R.drawable.cloudy
        else -> R.drawable.unknown_weather_condition
    }
}



