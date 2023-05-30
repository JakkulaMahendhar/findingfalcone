package com.universe.findingfalcone.data

import com.universe.findingfalcone.domain.model.FindResponse
import com.universe.findingfalcone.domain.model.Planet
import com.universe.findingfalcone.domain.model.Vehicle
import com.universe.findingfalcone.data.model.FindApiResponse
import com.universe.findingfalcone.data.model.PlanetsApiResponse
import com.universe.findingfalcone.data.model.VehiclesApiResponse

fun PlanetsApiResponse.mapToPlanet() = Planet(name, distance)

fun VehiclesApiResponse.mapToVehicle() = Vehicle(name, amount, maxDistance, speed)

fun FindApiResponse.mapToFindResponse() =
    when (status) {
        "success" -> FindResponse.Success(planetName!!)
        "false" -> FindResponse.Failure
        else -> throw SecurityException(error)
    }