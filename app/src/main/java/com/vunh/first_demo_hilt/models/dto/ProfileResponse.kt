package com.vunh.first_demo_hilt.models.dto

import com.google.gson.annotations.SerializedName
import com.vunh.first_demo_hilt.models.Profile
import com.vunh.first_demo_hilt.utils.extension.parseDate
import java.util.*

data class ProfileResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("avatar")
    val avatar: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("address")
    val address: String?,
    @SerializedName("totalCar")
    val totalCar: Int?,
    @SerializedName("createdCar")
    val createdCar: String?,
    @SerializedName("linkGoogle")
    val linkGoogle: Boolean = false,
    @SerializedName("linkFacebook")
    val linkFacebook: Boolean = false,
    @SerializedName("linkZalo")
    val linkZalo: Boolean = false,
    @SerializedName("password")
    val password: String?,
) {
    fun mapToProfile(
        phone: String = "",
        address: String = "",
        totalCar: Int = 0,
        createdCar: String? = null,
        linkGoogle: Boolean = false,
        linkFacebook: Boolean = false,
        linkZalo: Boolean = false,
        password: String = "123456",
    ): Profile = Profile(
        id = id,
        email = email,
        firstName = firstName,
        lastName = lastName,
        avatar = avatar,
        phone = phone,
        address = address,
        totalCar = totalCar,
        createdCar = createdCar?.parseDate(),
        linkGoogle = linkGoogle,
        linkFacebook = linkFacebook,
        linkZalo = linkZalo,
        password = password,
    )
}
