package com.example.calendar.presentation.screen

import WeatherResponse
import android.content.Context
import android.location.Geocoder
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.calendar.R
import com.example.calendar.data.NavRoutes
import com.example.calendar.utils.LocationUtil
import com.example.calendar.utils.RetrofitInstance
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun WeatherDisplay(
    navController: NavController
) {
    var weatherData by remember { mutableStateOf<WeatherResponse?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val apiKey = "ERtoam8JXYf21rCXIfEhd9w1gZVhLkU6"
    var locationKey by remember { mutableStateOf<String?>(null) } // Will be set when location is obtained
    val context = LocalContext.current

    //get location
    @Suppress("DEPRECATION")
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val locationUtil = LocationUtil(context)
            locationUtil.startLocationUpdates(onSuccess = { location ->
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses =
                    location?.let { geocoder.getFromLocation(it.latitude, location.longitude, 1) }
                Log.d("location", addresses.toString())
                val address = addresses?.get(0)
                if (address != null) {
                    locationKey = address.postalCode
                }
            }, onFailure = { error ->
                Log.e("WeatherDisplay", error.toString())
            })
        }
    }

    LaunchedEffect(locationKey) {
        coroutineScope.launch {
            try {
                val response = RetrofitInstance.api.getDailyForecast(locationKey.toString(), apiKey)
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
                locationKey?.let { key ->
                    navController.navigate("fiveDayForecast/$key")
                }
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
                contentDescription = condition,
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
        "Sunny" -> R.drawable.sunny
        "Mostly sunny" -> R.drawable.sunny
        "Partly sunny" -> R.drawable.sunny
        "Hazy sunshine" -> R.drawable.sunny
        "Cloudy" -> R.drawable.cloudy
        "Partly cloudy" -> R.drawable.intermittentclouds
        "Intermittent clouds" -> R.drawable.intermittentclouds
        "Snow" -> R.drawable.snowy
        "Mostly cloudy" -> R.drawable.cloudy
        "Cloudy" -> R.drawable.cloudy
        "Dreary" -> R.drawable.cloudy
        "Fog" -> R.drawable.cloudy
        "Showers" -> R.drawable.rainy
        "Mostly Cloudy w/ Showers" -> R.drawable.cloudy
        "Partly Sunny w/ Showers" -> R.drawable.cloudy
        "T-Storms" -> R.drawable.cloudy
        "Mostly Cloudy w/ T-Storms" -> R.drawable.cloudy
        "Partly Sunny w/ T-Storms" -> R.drawable.sunny
        "Rain" -> R.drawable.rainy
        "Flurries" -> R.drawable.snowy
        "Mostly Cloudy w/ Flurries" -> R.drawable.snowy
        "Partly Sunny w/ Flurries" -> R.drawable.snowy
        "Mostly Cloudy w/ Snow" -> R.drawable.snowy
        "Ice" -> R.drawable.snowy
        "Sleet" -> R.drawable.snowy
        "Freezing rain" -> R.drawable.grain
        "Rain and snow" -> R.drawable.grain
        "Hot" -> R.drawable.sunny
        "Cold" -> R.drawable.snowy
        "Windy" -> R.drawable.cloudy
        "Clear" -> R.drawable.sunny
        "Mostly clear" -> R.drawable.sunny
        "Partly cloudy" -> R.drawable.intermittentclouds
        "Intermittent clouds" -> R.drawable.intermittentclouds
        "Hazy moonlight" -> R.drawable.cloudy
        "Mostly cloudy" -> R.drawable.cloudy
        else -> R.drawable.unknown_weather_condition
    }
}



