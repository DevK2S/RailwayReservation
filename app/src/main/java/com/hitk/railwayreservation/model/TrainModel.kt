package com.hitk.railwayreservation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TrainModel(
    val trainNumber : Int = 0,
    val trainName : String ="",
    val startStation : String ="",
    val endStation : String ="",
):Parcelable
