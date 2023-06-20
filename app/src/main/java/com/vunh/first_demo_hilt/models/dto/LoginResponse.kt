package com.vunh.first_demo_hilt.models.dto

import com.google.gson.annotations.SerializedName
import com.vunh.first_demo_hilt.models.User

data class LoginResponse(
    @SerializedName("data") var data: User? = null,
    @SerializedName("support") var support: Any?,
)
