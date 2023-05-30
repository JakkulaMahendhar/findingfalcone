package com.universe.findingfalcone.data.model

import com.google.gson.annotations.SerializedName


data class FindApiResponse(
    @SerializedName("planet_name") val planetName: String?,
    val status: String?,
    val error: String?
)