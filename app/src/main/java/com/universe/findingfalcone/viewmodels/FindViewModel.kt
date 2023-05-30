package com.universe.findingfalcone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.universe.findingfalcone.domain.Repository
import com.universe.findingfalcone.data.model.FindApiResponse
import com.universe.findingfalcone.domain.model.FindResponse
import com.universe.findingfalcone.domain.model.Planet
import com.universe.findingfalcone.domain.model.Vehicle
import com.universe.findingfalcone.data.mapToFindResponse
import com.universe.findingfalcone.extensions.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class FindViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _viewState = MutableLiveData<FindResponse>()
    val viewState: LiveData<FindResponse> = _viewState

    private val _loading = SingleLiveEvent<Loading>()
    val loading: LiveData<Loading> = _loading

    private val _message = SingleLiveEvent<String>()
    val message: LiveData<String> = _message

    private var initialised = false

    fun init(planets: List<Planet>, vehicles: List<Vehicle>) {
        if (!initialised) {
            initialised = true
            _loading.postValue(Loading.Show)
            findPrince(planets, vehicles)
        }
    }

    private fun findPrince(planets: List<Planet>, vehicles: List<Vehicle>) {
        viewModelScope.launch {
            try {
                val findPrinceResponse = repository.findPrincess(planets, vehicles)
                handleFindPrinceResponse(findPrinceResponse)
            } catch (e: HttpException) {
                _message.postValue(e.message)
            } catch (e: IOException) {
                _message.postValue(e.message)
            } catch (e: Exception) {
                _message.postValue(e.message)
            }
        }
    }

    private fun handleFindPrinceResponse(findPrinceResponse: Response<FindApiResponse>) {
        if (findPrinceResponse.isSuccessful) {
            val response = findPrinceResponse.body()?.mapToFindResponse()
            _loading.postValue(Loading.Hide)
            _viewState.value = response
        } else {
            _message.postValue(findPrinceResponse.body()?.error)
        }

    }

    sealed class Loading {
        object Show : Loading()
        object Hide : Loading()
    }
}