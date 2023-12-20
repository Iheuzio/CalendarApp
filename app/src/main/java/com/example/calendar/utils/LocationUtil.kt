package com.example.calendar.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageAndVideo.equals
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly.equals
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageAndVideo.equals
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly.equals
import com.example.calendar.presentation.MainActivity

class LocationUtil(private val context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient
    private var locationCallback: LocationCallback? = null

    init {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    private val locationRequest: LocationRequest
        private get() {
            val locationRequest = LocationRequest.create()
            locationRequest.interval = 10000
            locationRequest.fastestInterval = 5000
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            return locationRequest
        }

    fun hasLocationPermission(): Boolean {
        val fineLocationPermission =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarseLocationPermission =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        return fineLocationPermission == PackageManager.PERMISSION_GRANTED || coarseLocationPermission == PackageManager.PERMISSION_GRANTED
    }

    fun startLocationUpdates(
        onSuccess: OnSuccessListener<Location?>,
        onFailure: OnFailureListener
    ) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            onFailure.onFailure(SecurityException("Location permission not granted"))
            return
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult.locations.isEmpty()) {
                    return
                }
                for (location in locationResult.locations) {
                    onSuccess.onSuccess(location)
                }
            }
        }
        createLocationRequest()?.let {
            fusedLocationClient.requestLocationUpdates(it,
                locationCallback as LocationCallback, context.mainLooper)
                .addOnFailureListener(onFailure)
        }
    }


    fun stopLocationUpdates() {
        if (locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback!!)
        }
    }
    private fun createLocationRequest(): LocationRequest? {
        val locationRequest = LocationRequest.create()
        locationRequest.interval =
            10000 // Set the desired interval for active location updates, in milliseconds
        locationRequest.fastestInterval =
            5000 // Set the fastest interval for location updates, in milliseconds
        locationRequest.priority =
            LocationRequest.PRIORITY_HIGH_ACCURACY // Set the priority of the request
        return locationRequest
    }

}