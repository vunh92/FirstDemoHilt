package com.vunh.first_demo_hilt.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "tbMaintenance")
data class Maintenance(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @SerializedName("userId")
    val userId: Int?,
    @SerializedName("typeMaintenance")
    var typeMaintenance: String?,
    @SerializedName("periodMaintenance")
    var periodMaintenance: Int?,
    @SerializedName("isPeriodMaintenance")
    var isPeriodMaintenance: Boolean = true,
    @SerializedName("distanceMaintenance")
    var distanceMaintenance: Int?,
    @SerializedName("isDistanceMaintenance")
    var isDistanceMaintenance: Boolean = true,
    @SerializedName("lastTimeMaintenance")
    var lastTimeMaintenance: Date?,
    @SerializedName("noteMaintenance")
    var noteMaintenance: String?,
    @SerializedName("isDelete")
    var isDelete: Boolean = false,
)
