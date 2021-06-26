package com.hitk.railwayreservation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TicketModel(
	val pnrNumber: String,
	val ticketState: TicketState,
	val trainNumber: Long,
	val source: String,
	val destination: String,
	val travelDate: String,
	val numberOfPassengers: Int,
	val totalAmount: Double,
) : Parcelable
