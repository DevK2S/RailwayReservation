package com.hitk.railwayreservation.model

import kotlinx.android.parcel.Parcelize

@Parcelize
data class AdminModel(
	val userType: UserType = UserType.ADMIN,
	val name: String = "",
	val email: String = "",
	val phoneNumber: String = "",
)
