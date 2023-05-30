package com.universe.findingfalcone.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.universe.findingfalcone.R
import com.universe.findingfalcone.domain.model.Vehicle
import com.universe.findingfalcone.databinding.ItemVehicleBinding

class VehicleAdapter(private val action: (vehicle: Vehicle) -> Unit) : RecyclerView.Adapter<VehicleAdapter.CustomAdapter>() {
    private var list: ArrayList<Vehicle> = arrayListOf()
    private lateinit var binding: ItemVehicleBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_vehicle, parent, false)
        binding = ItemVehicleBinding.bind(view)
        return CustomAdapter(binding.root)
    }

    override fun onBindViewHolder(holder: CustomAdapter, position: Int) {
        holder.bind(list[position], action)
    }

    override fun getItemCount(): Int = list.size

    // Add a list of items
    fun updateList(list: ArrayList<Vehicle>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class CustomAdapter(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(vehicle: Vehicle, action: (vehicle: Vehicle) -> Unit) {
            binding.shuttleImage.setImageResource(vehicle.getImage())
            binding.shuttleSpeedText.text = vehicle.speed.toString()
            binding.shuttleContainer.setOnClickListener { action(vehicle) }
        }
    }
}