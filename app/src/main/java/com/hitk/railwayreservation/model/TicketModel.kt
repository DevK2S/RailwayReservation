package com.hitk.railwayreservation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TicketModel(
	var pnrNumber: String,
	var ticketState: TicketState,
	var trainNumber: Long,
	var trainName: String,
	var source: String,
	var destination: String,
	var deptDate: Long,
	var deptTime: String,
	var arrivalDate: Long,
	var arrivalTime: String,
	var numberOfPassengers: Int,
	var totalAmount: Double,
	var paymentMade: Boolean,
	var paymentDate: Long
) : Parcelable {

	constructor() : this("", TicketState.BOOKED, 0, "", "", "", 0, "", 0, "", 0, 0.0, false, 0)
}
