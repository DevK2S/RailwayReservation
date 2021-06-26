package com.hitk.railwayreservation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TrainModel(
	val number: Long,
	val name: String,
	val source: String,
	val departureTime: String,
	val destination: String,
	val arrivalTime: String,
	val numberOfSeats: Int,
	val seatsAvailable: Int,
	val seatType: SeatType,
	val fare: Double,
) : Parcelable