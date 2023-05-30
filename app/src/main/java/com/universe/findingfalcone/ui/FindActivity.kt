package com.universe.findingfalcone.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.universe.findingfalcone.R
import com.universe.findingfalcone.domain.model.FindResponse
import com.universe.findingfalcone.domain.model.Planet
import com.universe.findingfalcone.domain.model.Vehicle
import com.universe.findingfalcone.extensions.observe
import com.universe.findingfalcone.databinding.ActivityFindBinding
import com.universe.findingfalcone.extensions.toast
import com.universe.findingfalcone.viewmodels.FindViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FindActivity : AppCompatActivity() {

    private lateinit var findBinding: ActivityFindBinding
    private lateinit var findViewModel: FindViewModel

    companion object {
        private const val PLANETS = "planets"
        private const val VEHICLES = "vehicles"

        fun intent(context: Context, planets: ArrayList<Planet>, vehicles: ArrayList<Vehicle>) =
            Intent(context, FindActivity::class.java).apply {
                putExtra(PLANETS, planets)
                putParcelableArrayListExtra(VEHICLES, vehicles)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findBinding = ActivityFindBinding.inflate(layoutInflater)
        setContentView(findBinding.root)
        findViewModel = ViewModelProvider(this)[FindViewModel::class.java]
        findViewModel.message.observe(this, onChange = ::showMessage)
        findViewModel.loading.observe(this, onChange = ::renderLoading)
        findViewModel.viewState.observe(this, onChange = ::renderView)

        val selectedPlanet =
            requireNotNull(intent.getParcelableArrayListExtra<Planet>(PLANETS)) { "Planets list not provided" }
        val selectedVehicle =
            requireNotNull(intent.getParcelableArrayListExtra<Vehicle>(VEHICLES)) { "Vehicle list not provided" }

        findViewModel.init(selectedPlanet, selectedVehicle)
    }

    private fun renderLoading(loading: FindViewModel.Loading) {
        when (loading) {
            FindViewModel.Loading.Show -> findBinding.progressLoading.visibility = View.VISIBLE
            FindViewModel.Loading.Hide -> findBinding.progressLoading.visibility = View.GONE
        }
    }

    private fun renderView(state: FindResponse) {
        when (state) {
            is FindResponse.Success -> findBinding.resultText.text =
                getString(R.string.princes_found_on, state.planetName)

            is FindResponse.Failure -> findBinding.resultText.text =
                getString(R.string.princes_not_found)
        }
        findBinding.resultText.visibility = View.VISIBLE
    }

    private fun showMessage(message: String) {
        this.toast(message)
    }
}