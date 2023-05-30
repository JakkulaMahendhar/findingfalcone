package com.universe.findingfalcone.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.universe.findingfalcone.databinding.ActivityMainBinding
import com.universe.findingfalcone.domain.model.Planet
import com.universe.findingfalcone.domain.model.Vehicle
import com.universe.findingfalcone.extensions.observe
import com.universe.findingfalcone.extensions.toast
import com.universe.findingfalcone.viewmodels.MainViewModel
import com.universe.findingfalcone.viewmodels.MainViewModel.ViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.viewState.observe(this, onChange = ::renderView)
        viewModel.message.observe(this, onChange = ::showMessage)
        viewModel.navigation.observe(this, onChange = ::navigate)
        viewModel.loading.observe(this, onChange = ::renderLoading)

        binding.planet1Layout.setOnClickListener { viewModel.onPlanetClicked(binding.planet1Layout.getPlanetName()) }
        binding.planet2Layout.setOnClickListener { viewModel.onPlanetClicked(binding.planet2Layout.getPlanetName()) }
        binding.planet3Layout.setOnClickListener { viewModel.onPlanetClicked(binding.planet3Layout.getPlanetName()) }
        binding.planet4Layout.setOnClickListener { viewModel.onPlanetClicked(binding.planet4Layout.getPlanetName()) }
        binding.planet5Layout.setOnClickListener { viewModel.onPlanetClicked(binding.planet5Layout.getPlanetName()) }
        binding.planet6Layout.setOnClickListener { viewModel.onPlanetClicked(binding.planet6Layout.getPlanetName()) }

        binding.btnFind.setOnClickListener { viewModel.onFindClicked() }

        viewModel.init()
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == Activity.RESULT_OK){
                val planet = requireNotNull(planetMapping(it.data)) { "planet is missing" }
                viewModel.onActivityResult(
                    planet,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        it.data?.getParcelableExtra(VehicleSelectionActivity.VEHICLE, Vehicle::class.java)
                    } else{
                        it.data?.getParcelableExtra(VehicleSelectionActivity.VEHICLE)
                    }
                )
            }
        }

    private fun planetMapping(data: Intent?) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            data?.getParcelableExtra(VehicleSelectionActivity.PLANET, Planet::class.java)
        } else {
            data?.getParcelableExtra<Planet>(VehicleSelectionActivity.PLANET)
        }

    private fun renderView(state: ViewState) {
        val planets = state.planetVehicle.keys.toList()
        binding.planet1Layout.visibility = View.VISIBLE
        binding.planet1Name.text = planets[0].name
        state.planetVehicle[planets[0]]?.let { binding.planet1Shuttle.setImageResource(it.getImage()) }

        binding.planet2Layout.visibility = View.VISIBLE
        binding.planet2Name.text = planets[1].name
        state.planetVehicle[planets[1]]?.let { binding.planet2Shuttle.setImageResource(it.getImage()) }

        binding.planet3Layout.visibility = View.VISIBLE
        binding.planet3Name.text = planets[2].name
        state.planetVehicle[planets[2]]?.let { binding.planet3Shuttle.setImageResource(it.getImage()) }

        binding.planet4Layout.visibility = View.VISIBLE
        binding.planet4Name.text = planets[3].name
        state.planetVehicle[planets[3]]?.let { binding.planet4Shuttle.setImageResource(it.getImage()) }

        binding.planet5Layout.visibility = View.VISIBLE
        binding.planet5Name.text = planets[4].name
        state.planetVehicle[planets[4]]?.let { binding.planet5Shuttle.setImageResource(it.getImage()) }

        binding.planet6Layout.visibility = View.VISIBLE
        binding.planet6Name.text = planets[5].name
        state.planetVehicle[planets[5]]?.let { binding.planet6Shuttle.setImageResource(it.getImage()) }

        binding.btnFind.isEnabled = state.isFindEnabled
    }

    private fun navigate(event: MainViewModel.NavigationEvent) {
        when (event) {
            is MainViewModel.NavigationEvent.ShowVehicleSelection ->
                getResult.launch(VehicleSelectionActivity.intent(
                    this,
                    planet = event.selectedPlanet,
                    vehicles = event.vehicles
                ))

            is MainViewModel.NavigationEvent.ShowResult -> {
                startActivity(FindActivity.intent(this, event.planets, event.vehicles))
                finish()
            }
        }
    }

    private fun renderLoading(loading: MainViewModel.Loading) {
        when (loading) {
            MainViewModel.Loading.Show -> binding.progressLoading.visibility = View.VISIBLE
            MainViewModel.Loading.Hide -> binding.progressLoading.visibility = View.GONE
        }
    }

    private fun showMessage(message: String) {
        this.toast(message)
    }

    private fun ViewGroup.getPlanetName(): String = (getChildAt(1) as TextView).text.toString()
}