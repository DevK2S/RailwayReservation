package com.hitk.railwayreservation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TrainModel(
	val number: Long,
	val name: String,
	val source: String,
	val departureDate: Long,
	val departureTime: String,
	val destination: String,
	val arrivalDate: Long,
	val arrivalTime: String,
	val numberOfSeats: Int,
	val seatsAvailable: Int,
	val seatType: SeatType,
	val fare: Double,
) : Parcelable {
	
	constructor() : this(0, "", "", 0, "", "", 0, "", 0, 0, SeatType.GENERAL, 0.0)
}