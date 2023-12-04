package com.example.calendar.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource

class LocationUtil(private val context: Context) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fun getLastLocation(onSuccess: (Location) -> Unit, onFailure: () -> Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            onFailure()
            return
        }

        val cancellationTokenSource = CancellationTokenSource()

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let { onSuccess(it) } ?: onFailure()
        }.addOnFailureListener {
            onFailure()
        }
    }
}
