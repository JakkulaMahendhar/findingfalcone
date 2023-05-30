package com.universe.findingfalcone.data.model

import com.google.gson.annotations.SerializedName

data class VehiclesApiResponse(
    val name: String,
    @SerializedName("total_no") val amount: Int,
    @SerializedName("max_distance") val maxDistance: Int,
    val speed: Int
)