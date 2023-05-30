package com.universe.findingfalcone.data

import com.universe.findingfalcone.data.model.FindApiRequest
import com.universe.findingfalcone.data.model.FindApiResponse
import com.universe.findingfalcone.data.model.PlanetsApiResponse
import com.universe.findingfalcone.data.model.TokenApiResponse
import com.universe.findingfalcone.data.model.VehiclesApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface Service {

    @Headers("Accept: application/json")
    @POST("/token")
    suspend fun getToken() : Response<TokenApiResponse>

    @GET("/planets")
    suspend fun getPlanets(): Response<List<PlanetsApiResponse>>

    @GET("/vehicles")
    suspend fun getVehicles(): Response<List<VehiclesApiResponse>>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("/find")
    suspend fun findPrincess(@Body body: FindApiRequest) : Response<FindApiResponse>
}