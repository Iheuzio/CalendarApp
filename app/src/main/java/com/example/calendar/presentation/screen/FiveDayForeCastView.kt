package com.example.calendar.presentation.screen

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

@Composable
fun FiveDayForecastScreen(navController: NavController) {
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
            items(forecastData) { data ->
                ForecastItem(
                    time = data.time,
                    temperature = data.temperature,
                    condition = data.condition,
                    iconId = data.iconId
                )
            }
        }
    }
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

// Placeholder data
data class ForecastData(
    val time: String,
    val temperature: String,
    val condition: String,
    val iconId: Int
)
