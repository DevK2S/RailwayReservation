package com.hitk.railwayreservation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel(
    var userType: UserType,
    var name: String,
    var email: String,
    var phoneNumber: String,
    var address: String,
    var outstandingBalance: Double,
    var ticketNumbers: String,
) : Parcelable {

    constructor(
        name: String, email: String, phoneNumber: String, address: String, ticketNumbers: String
    ) : this(UserType.PASSENGER, name, email, phoneNumber, address, 0.0, ticketNumbers)

    constructor(
    ) : this(UserType.PASSENGER, "", "", "", "", 0.0, "")
}