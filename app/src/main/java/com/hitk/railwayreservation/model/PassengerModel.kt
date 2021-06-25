package com.hitk.railwayreservation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PassengerModel(
	val userType: UserType = UserType.PASSENGER,
	val name: String = "",
	val email: String = "",
	val phoneNumber: String = "",
	val address: String = "",
	val outstandingBalance: Double = 0.0,
):Parcelable