package com.vunh.first_demo_hilt.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class TestModel(
    var type: Int = 1,
    var title: String?,
    var content: String?,
)
