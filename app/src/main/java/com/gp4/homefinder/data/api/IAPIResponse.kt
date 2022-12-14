package com.gp4.homefinder.data.api

import com.gp4.homefinder.data.models.DistanceResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface IAPIResponse {

    @GET("json")
    suspend fun getDistances(
        @Query("origins") oriLatLng:String,
        @Query("destinations") desLatLngS:String,
        @Query("key") key:String = "AIzaSyDiVSCYbrAE8ida_JjyXZoNxYVJZB0yxYU"
    ):DistanceResponse
}