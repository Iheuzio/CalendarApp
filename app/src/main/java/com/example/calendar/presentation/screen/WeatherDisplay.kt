package com.example.calendar.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.calendar.R
import com.example.calendar.data.NavRoutes

@Composable
fun WeatherDisplay(
    navController: NavController,
    temperature: String = "21°C",
    weatherCondition: String = "Sunny",
    iconId: Int = R.drawable.sunny,
    lastUpdated: String = "10:00 AM"
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
            .clickable(onClick = {
                navController.navigate(NavRoutes.FiveDayForecast.route)
            })
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = weatherCondition,
            modifier = Modifier.size(48.dp)
        )

        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(text = temperature)
            Text(text = weatherCondition)
            Text(text = "Last updated: $lastUpdated")
        }
    }
}
