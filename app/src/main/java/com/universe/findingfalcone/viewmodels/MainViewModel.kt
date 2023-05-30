package com.universe.findingfalcone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.universe.findingfalcone.ui.utils.LocalProperties
import com.universe.findingfalcone.domain.Repository
import com.universe.findingfalcone.domain.model.Planet
import com.universe.findingfalcone.data.model.PlanetsApiResponse
import com.universe.findingfalcone.domain.model.Vehicle
import com.universe.findingfalcone.data.model.TokenApiResponse
import com.universe.findingfalcone.data.model.VehiclesApiResponse
import com.universe.findingfalcone.data.mapToPlanet
import com.universe.findingfalcone.data.mapToVehicle
import com.universe.findingfalcone.extensions.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository, private val localProperties: LocalProperties) : ViewModel() {

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    private val _message = SingleLiveEvent<String>()
    val message: LiveData<String> = _message

    private val _loading = SingleLiveEvent<Loading>()
    val loading: LiveData<Loading> = _loading

    private val _navigation = SingleLiveEvent<NavigationEvent>()
    val navigation: LiveData<NavigationEvent> = _navigation

    private lateinit var planets: List<Planet>
    private lateinit var vehicles: List<Vehicle>

    private val selectedItems = mutableMapOf<Planet, Vehicle?>()

    fun init(){
        getToken()
        fetchData()
    }

    private fun getToken(){
        viewModelScope.launch {
           val response =  repository.getToken()
            handleTokenResponse(response)
        }

    }

    private fun fetchData() {
        viewModelScope.launch {
            try {
                _loading.postValue(Loading.Show)
                val planetResponse = repository.getPlanets()
                val vehicleResponse = repository.getVehicles()
                handlePlanetResponse(planetResponse, vehicleResponse)
            } catch (e: HttpException) {
                _message.postValue(e.message)
            } catch (e: IOException) {
                _message.postValue(e.message)
            } catch (e: Exception) {
                _message.postValue(e.message)
            }
        }
    }

    private fun handlePlanetResponse(planetResponse: Response<List<PlanetsApiResponse>>, vehicleResponse: Response<List<VehiclesApiResponse>>) {
        var planets = emptyList<Planet>()
        var vehicles = emptyList<Vehicle>()
        if(planetResponse.isSuccessful) {
           planets = planetResponse.body()?.map {
               it.mapToPlanet()
           }!!
            vehicles = vehicleResponse.body()?.map {
                it.mapToVehicle()
            }!!

            val data = Pair(planets,vehicles)
            onSuccess(data)
        }
    }

    fun onActivityResult(planet: Planet, selectedVehicle: Vehicle?) {
        selectedItems[planet] = selectedVehicle
        _viewState.postValue(ViewState(selectedItems, isFindEnabled()))
    }

    private fun onSuccess(data: Pair<List<Planet>, List<Vehicle>>) {
        planets = data.first
        vehicles = data.second
        planets.forEach {
            selectedItems[it] = null
        }
        _loading.postValue(Loading.Hide)
        _viewState.postValue(ViewState(selectedItems, isFindEnabled()))
    }

    private fun isFindEnabled(): Boolean =
        selectedItems.values.filterNotNull().count() == 4

    data class ViewState(val planetVehicle: Map<Planet, Vehicle?>, val isFindEnabled: Boolean)


    private fun handleTokenResponse(response : Response<TokenApiResponse>){
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                localProperties.token = resultResponse.token
            }
        }else{
            _message.postValue(response.message())
        }
    }

    fun onPlanetClicked(planetName: String) {
        if (isFindEnabled()) {
            //TODO: move string to resources
            _message.postValue("You have selected enough planet, click Find")
            return
        }
        val planet = planets.first { it.name == planetName }
        _navigation.value =
            NavigationEvent.ShowVehicleSelection(planet, ArrayList(getAvailableVehiclesFor(planet)))
    }

    private fun getAvailableVehiclesFor(planet: Planet): List<Vehicle> {
        val list = mutableListOf<Vehicle>()
        for (item in vehicles) {
            when (val count = selectedItems.values.count { it == item }) {
                0 -> list.add(item)
                else -> {
                    if (item.amount - count > 0) {
                        list.add(item)
                    }
                }
            }
        }

        return list.filter { it.maxDistance >= planet.distance }
    }

    fun onFindClicked() {
        _navigation.value = NavigationEvent.ShowResult(
            ArrayList(selectedItems.filter { it.value != null }.keys),
            ArrayList(selectedItems.values.mapNotNull { it })
        )
    }

    sealed class NavigationEvent {
        data class ShowVehicleSelection(val selectedPlanet: Planet, val vehicles: ArrayList<Vehicle>) : NavigationEvent()
        data class ShowResult(val planets: ArrayList<Planet>, val vehicles: ArrayList<Vehicle>) : NavigationEvent()
    }

    sealed class Loading {
        object Show : Loading()
        object Hide : Loading()
    }

}