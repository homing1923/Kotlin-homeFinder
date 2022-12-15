package com.gp4.homefinder.data

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import java.util.*

// This class is architected as a Singleton
class LocationHelper private constructor() {
    // ----------------
    // tag
    // ----------------
    private val TAG = "LOCATION-HELPER"

    // ----------------
    // singleton
    // ----------------
    companion object {
        val instance = LocationHelper()
    }

    var locationPermissionGranted = false

    val REQUEST_CODE_LOCATION = 1234


    // ----------------
    // locations properties
    // ----------------
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder

    var lastLocation : Location? = null
    var realCurrentLocation : Location? = null
    private val locationRequest : CurrentLocationRequest = CurrentLocationRequest.Builder().build()

    init {
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation(context: Context) : Location?{
        if (locationPermissionGranted){
            fusedLocationClient = getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation.addOnSuccessListener {location ->

                if (location != null){
                    this.lastLocation = location
                    Log.e(TAG, "getLastLocation: last location obtained : ${this.lastLocation}" )
                }else{
                    Log.e(TAG, "getLastLocation: Not able to get last known location" )
                }

            }.addOnFailureListener {
                Log.e(TAG, "getLastLocation: Failed to get the last known location ${it}" )
            }

            return this.lastLocation

        }else {
            Log.e(TAG, "getLastLocation: No location permission granted")
            requestLocationPermission(context)
        }

        return null
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(context: Context) : Location?{
        val localCurrentLocation : Location
        if (locationPermissionGranted){
            fusedLocationClient = getFusedLocationProviderClient(context)
            fusedLocationClient.getCurrentLocation(locationRequest, object : CancellationToken(){
                override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
                    return this
                }
                override fun isCancellationRequested(): Boolean {
                    return false
                }

            })
                .addOnSuccessListener {location ->
                    if (location != null){
                        realCurrentLocation = location
                        Log.e(TAG, "getCurrentLocation: current location obtained : ${realCurrentLocation}" )
                    }else{
                        Log.e(TAG, "getCurrentLocation: Not able to get last known location" )
                    }
                }
        }
        return realCurrentLocation
    }

    fun performForwardGeocoding(context: Context, location: com.google.android.gms.maps.model.LatLng): Address? {
        Log.d("D1", "LocationHelper: doing forwardGeocode")
        geocoder = Geocoder(context, Locale.getDefault())
        val decodedLocation = geocoder.getFromLocation(location.latitude, location.longitude,1)
        return if(decodedLocation != null){
            Log.d("D1", "LocationHelper: forwardGeocode success")
            decodedLocation[0]
        }else{
            Log.d("D1", "Decoding Address error")
            null
        }
    }

    // permission helpers

    //check app owns fine location per or not
    private fun hasFineLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    //check app owns coarse location per or not
    private fun hasCoarseLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context.applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    //a general entry for checking all permission required
    fun checkPermissions(context: Context) {
        if (hasFineLocationPermission(context) && hasCoarseLocationPermission(context)) {
            this.locationPermissionGranted = true
        }
        Log.d(TAG, "checkPermissions: Are location permissions granted? : " + this.locationPermissionGranted)

        if (!this.locationPermissionGranted) {
            Log.d(TAG, "checkPermissions: Permissions not granted, so requesting permission now...")
            requestLocationPermission(context)
        }
    }

    //if no, ask
    fun requestLocationPermission(context: Context) {
        val listOfRequiredPermission
                = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

        ActivityCompat.requestPermissions((context as Activity), listOfRequiredPermission, REQUEST_CODE_LOCATION)
    }

}
