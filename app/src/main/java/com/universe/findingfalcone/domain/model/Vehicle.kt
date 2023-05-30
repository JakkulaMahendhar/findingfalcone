package com.universe.findingfalcone.domain.model

import android.os.Parcelable
import com.universe.findingfalcone.R
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Vehicle(
    val name: String,
    val amount: Int,
    val maxDistance: Int,
    val speed: Int
) : Parcelable {
    fun getImage() = when (name) {
        "Space pod" -> R.drawable.ic_spaceship1
        "Space rocket" -> R.drawable.ic_spaceship2
        "Space shuttle" -> R.drawable.ic_spaceship3
        "Space ship" -> R.drawable.ic_spaceship4
        else -> throw NoSuchElementException("unknown vehicle")
    }
}