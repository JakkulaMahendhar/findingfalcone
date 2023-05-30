package com.universe.findingfalcone.data

import com.universe.findingfalcone.ui.utils.LocalProperties
import com.universe.findingfalcone.data.model.FindApiRequest
import com.universe.findingfalcone.data.model.FindApiResponse
import com.universe.findingfalcone.domain.model.Planet
import com.universe.findingfalcone.data.model.PlanetsApiResponse
import com.universe.findingfalcone.domain.model.Vehicle
import com.universe.findingfalcone.data.model.TokenApiResponse
import com.universe.findingfalcone.data.model.VehiclesApiResponse
import com.universe.findingfalcone.domain.Repository
import retrofit2.Response
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val service: Service, private val localProperties: LocalProperties) :
    Repository {

    override suspend fun getToken(): Response<TokenApiResponse> {
        return service.getToken()
    }

    override suspend fun getPlanets(): Response<List<PlanetsApiResponse>> {
        return service.getPlanets()
    }

    override suspend fun getVehicles(): Response<List<VehiclesApiResponse>> {
        return service.getVehicles()
    }

    override suspend fun findPrincess(planets: List<Planet>, vehicles: List<Vehicle>): Response<FindApiResponse> {
        return service.findPrincess(FindApiRequest(localProperties.token.toString(),planets.map { it.name }, vehicles.map { it.name }))
    }


}