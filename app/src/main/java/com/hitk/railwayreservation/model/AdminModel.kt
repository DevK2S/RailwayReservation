package com.hitk.railwayreservation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AdminModel(
	val userType: UserType,
	val name: String,
	val email: String,
	val phoneNumber: String,
) : Parcelable {
	
	constructor(name: String, email: String, phoneNumber: String) : this(
		UserType.ADMIN, name, email, phoneNumber
	)
}
