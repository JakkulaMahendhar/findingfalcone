package com.universe.findingfalcone.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.universe.findingfalcone.ui.adapters.VehicleAdapter
import com.universe.findingfalcone.domain.model.Planet
import com.universe.findingfalcone.domain.model.Vehicle
import com.universe.findingfalcone.extensions.observe
import com.universe.findingfalcone.databinding.ActivityVehicleSelectionBinding
import com.universe.findingfalcone.viewmodels.VehicleSelectionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VehicleSelectionActivity : AppCompatActivity() {

    private lateinit var vehicleSelectionBinding: ActivityVehicleSelectionBinding
    private lateinit var viewModel: VehicleSelectionViewModel

    private val programsAdapter by lazy { VehicleAdapter(viewModel::onItemClicked) }

    companion object {
        const val PLANET = "planet"
        const val VEHICLE = "vehicle"

        fun intent(context: Context, planet: Planet, vehicles: ArrayList<Vehicle>) =
            Intent(context, VehicleSelectionActivity::class.java).apply {
                putExtra(PLANET, planet)
                putParcelableArrayListExtra(VEHICLE, vehicles)
            }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vehicleSelectionBinding = ActivityVehicleSelectionBinding.inflate(layoutInflater)
        setContentView(vehicleSelectionBinding.root)
        viewModel = ViewModelProvider(this).get(VehicleSelectionViewModel::class.java)


        viewModel.viewState.observe(this, onChange = ::renderView)
        viewModel.navigation.observe(this, onChange = ::navigate)

        vehicleSelectionBinding.vehicleRecyclerView.apply {
            layoutManager =
                StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            adapter = programsAdapter
        }

        val selectedPlanet = requireNotNull(intent.getParcelableExtra<Planet>(PLANET)) { "no planet selected" }
        viewModel.init(selectedPlanet, intent.getParcelableArrayListExtra(VEHICLE) ?: arrayListOf())

    }

    private fun renderView(state: VehicleSelectionViewModel.ViewState) {
        vehicleSelectionBinding.emptyText.visibility = View.INVISIBLE
        vehicleSelectionBinding.vehicleRecyclerView.visibility = View.VISIBLE
        programsAdapter.updateList(state.vehicles)
    }

    private fun navigate(event: VehicleSelectionViewModel.NavigationEvent) {
        val intent = Intent().apply {
            putExtra(PLANET, event.planet)
            putExtra(VEHICLE, event.vehicle)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}