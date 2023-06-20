package com.vunh.first_demo_hilt.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class HistoryMaintenance(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("userId")
    var userId: Int?,
    @SerializedName("timeMaintenance")
    var timeMaintenance: Date?,
)
