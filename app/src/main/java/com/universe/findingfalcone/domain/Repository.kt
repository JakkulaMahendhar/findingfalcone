package com.universe.findingfalcone.domain

import com.universe.findingfalcone.data.model.FindApiResponse
import com.universe.findingfalcone.domain.model.Planet
import com.universe.findingfalcone.data.model.PlanetsApiResponse
import com.universe.findingfalcone.domain.model.Vehicle
import com.universe.findingfalcone.data.model.TokenApiResponse
import com.universe.findingfalcone.data.model.VehiclesApiResponse
import retrofit2.Response

interface Repository {

    suspend fun getToken() : Response<TokenApiResponse>

    suspend fun getPlanets() : Response<List<PlanetsApiResponse>>

    suspend fun getVehicles() : Response<List<VehiclesApiResponse>>

    suspend fun findPrincess(planets: List<Planet>, vehicles: List<Vehicle>) : Response<FindApiResponse>
}