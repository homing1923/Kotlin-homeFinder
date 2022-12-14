package com.gp4.homefinder.data

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.android.gms.location.Priority.PRIORITY_BALANCED_POWER_ACCURACY
import com.google.type.LatLng
import java.util.*

// This class is architected as a Singleton
class LocalHelper private constructor() {
    // ----------------
    // tag
    // ----------------
    private val TAG = "LOCATION-HELPER"

    // ----------------
    // singleton
    // ----------------
    companion object {
        val instance = LocalHelper()
    }

    var locationPermissionGranted = false

    val REQUEST_CODE_LOCATION = 1234


    // ----------------
    // locations properties
    // ----------------
    // TODO: Add properties relating to location services
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    var currentLocation : MutableLiveData<Location>? = MutableLiveData<Location>()
    private val locationRequest : com.google.android.gms.location.LocationRequest =
        com.google.android.gms.location.LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY,3000).build()

    init {
        // TODO: Instantiate location related variables here
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation(context: Context) : MutableLiveData<Location>?{
        if (locationPermissionGranted){
            fusedLocationClient = getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation.addOnSuccessListener {location ->

                if (location != null){
                    this.currentLocation!!.value = location
                    Log.e(TAG, "getLastLocation: last location obtained : ${this.currentLocation}", )
                }else{
                    Log.e(TAG, "getLastLocation: Not able to get last known location", )
                }

            }.addOnFailureListener {
                Log.e(TAG, "getLastLocation: Failed to get the last known location ${it}", )
            }

            return this.currentLocation

        }else {
            Log.e(TAG, "getLastLocation: No location permission granted",)
            requestLocationPermission(context)
        }

        return null
    }

    @SuppressLint("MissingPermission")
    fun requestLocationUpdates(context: Context, locationCallback: LocationCallback){
        if (locationPermissionGranted){
            try{
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
            }catch (ex: Exception){
                Log.e(TAG, "requestLocationUpdates: Failed to get lcoation updates ${ex}", )
            }
        }else{
            Log.e(TAG, "requestLocationUpdates: No permission", )
        }
    }

    fun stopLocationUpdates(context: Context, locationCallback: LocationCallback){
        try{
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }catch (ex: Exception){
            Log.e(TAG, "stopLocationUpdates: Failed to stop lcoation updates ${ex}", )
        }
    }

    /* ----------------------------- START LOCATION SERVICES CODE -------------------------------*/

//    // TODO: Functions relating to location services
//
//    @SuppressLint("MissingPermission")
//    fun getLastLocation (context: Context): MutableLiveData<Location>?{
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//        var locallocation : Location
//        if(locationPermissionGranted){
//            fusedLocationClient.lastLocation
//                .addOnSuccessListener { location ->
//                    if(location != null){
//                        locallocation = location
//                    }
//                }
//                .addOnFailureListener { location ->
//                    Log.d("D1", "Get Last Location Fail")
//                }
//            return locallocation
//
//        }else{
//            return null
//        }
//    }
    fun performForwardGeocoding(context: Context, location: LatLng): Address? {
        geocoder = Geocoder(context, Locale.getDefault())
        val decodedLocation = geocoder.getFromLocation(location.latitude, location.longitude,1)
    if(decodedLocation != null){
        return decodedLocation[0]
    }else{
        Log.d("D1", "Decoding Address error")
        return null
    }

//    fun calcDistance(location1: LatLng, location2: LatLng){
//    }

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
