package com.example.foodiemate.data.api

import com.google.gson.annotations.SerializedName

data class OverpassResponse(
    @SerializedName("elements")
    val elements: List<OverpassElement>
)

data class OverpassElement(
    @SerializedName("id")
    val id: Long,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double,
    @SerializedName("tags")
    val tags: Map<String, String>?
)

