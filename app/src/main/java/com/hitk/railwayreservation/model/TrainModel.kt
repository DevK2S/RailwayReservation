package com.hitk.railwayreservation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TrainModel(
    var number: Long,
    var name: String,
    var source: String,
    var departureDate: Long,
    var departureTime: String,
    var destination: String,
    var arrivalDate: Long,
    var arrivalTime: String,
    var numberOfSeats: Int,
    var seatsAvailable: Int,
    var seatType: SeatType,
    var fare: Double,
) : Parcelable {
	
	constructor() : this(0, "", "", 0, "", "", 0, "", 0, 0, SeatType.GENERAL, 0.0)
}