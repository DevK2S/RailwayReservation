package com.hitk.railwayreservation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PassengerModel(
	val userType: UserType,
	val name: String,
	val email: String,
	val phoneNumber: String,
	val address: String,
	val outstandingBalance: Double,
	val ticketNumbers: ArrayList<Long>,
) : Parcelable {
	
	constructor(
		name: String, email: String, phoneNumber: String, address: String
	) : this(UserType.PASSENGER, name, email, phoneNumber, address, 0.0, ArrayList())
}