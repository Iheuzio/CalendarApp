package com.example.calendar

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
@Composable
fun ConfirmButton(onConfirm: () -> Unit) {
    TextButton(
        onClick = onConfirm
    ) {
        Text(LocalContext.current.getStringResource(R.string.confirm))
    }
}


// Replace with your compose nav function
@Composable
fun EventCreationButton(isEventCreationDialogVisible: MutableState<Boolean>) {
    FloatingActionButton(
        onClick = { isEventCreationDialogVisible.value = true },
        modifier = Modifier
            .padding(16.dp)
            .size(56.dp),
        content = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = LocalContext.current.getStringResource(R.string.create_event),
                tint = Color.White
            )
        }
    )
}

@Composable
fun DismissButton(onDismiss: () -> Unit) {
    TextButton(
        onClick = onDismiss
    ) {
        Text(LocalContext.current.getStringResource(R.string.cancel))
    }
}
