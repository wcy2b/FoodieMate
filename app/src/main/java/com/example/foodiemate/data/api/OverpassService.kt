package com.example.foodiemate.data.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OverpassService {
    // Query format: [out:json];node["amenity"="restaurant"](around:radius,lat,lon);out;
    @GET("interpreter")
    fun searchNearbyRestaurants(
        @Query("data") data: String
    ): Call<OverpassResponse>
}

