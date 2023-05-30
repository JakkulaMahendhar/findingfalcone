package com.universe.findingfalcone.data.model

import com.google.gson.annotations.SerializedName

data class TokenApiResponse(
    @SerializedName("token")
    val token: String
)