package com.vunh.first_demo_hilt.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tbSetting")
data class Setting(
    @PrimaryKey
    @SerializedName("id")
    val id: Int?,
    @SerializedName("outInRestrictedArea")
    var outInRestrictedArea: Boolean,
    @SerializedName("forgotTurnOffCar")
    var forgotTurnOffCar: Boolean,
    @SerializedName("disconnectedOverAnHour")
    var disconnectedOverAnHour: Boolean,
    @SerializedName("language")
    var language: String,
    @SerializedName("formatDate")
    var formatDate: String,
    @SerializedName("timeRefreshData")
    var timeRefreshData: Int,
)
