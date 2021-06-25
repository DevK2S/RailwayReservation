package com.hitk.railwayreservation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AdminModel(
	val userType: UserType = UserType.ADMIN,
	val name: String = "",
	val email: String = "",
	val phoneNumber: String = "",
):Parcelable
