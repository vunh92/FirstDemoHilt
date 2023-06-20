package com.vunh.first_demo_hilt.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class News(
    @SerializedName("type")
    var type: Int = 1,
    @SerializedName("time")
    var time: Date?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("content")
    var content: String?,
    @SerializedName("isRead")
    var isRead: Boolean = true,
    @SerializedName("isDelete")
    var isDelete: Boolean = false,
)
