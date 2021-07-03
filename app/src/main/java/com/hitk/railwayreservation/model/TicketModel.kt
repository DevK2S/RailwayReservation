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
	var DeptDate: Long,
	var DeptTime: String,
	var ArrivalDate: Long,
	var ArrivalTime: String,
	var numberOfPassengers: Int,
	var totalAmount: Double,
) : Parcelable {

	constructor() : this("", TicketState.BOOKED, 0, "", "", "", 0, "", 0, "", 0, 0.0)
}
