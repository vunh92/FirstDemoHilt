package com.vunh.first_demo_hilt.models

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import java.util.Date

@Entity(tableName = "tbUser")
data class User (
    @SerializedName("id") val id: Int? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("first_name") val firstName: String? = null,
    @SerializedName("last_name") val lastName: String? = null,
    @SerializedName("avatar") val avatar: String? = null,
) {
    fun mapToProfile(
        phone: String = "",
        address: String = "",
        totalCar: Int = 0,
        createdCar: Date? = null,
        linkGoogle: Boolean = false,
        linkFacebook: Boolean = false,
        linkZalo: Boolean = false,
        password: String = "123456",
    ): Profile
    = Profile(
        id = id,
        email = email,
        firstName = firstName,
        lastName = lastName,
        avatar = avatar,
        phone = phone,
        address = address,
        totalCar = totalCar,
        createdCar = createdCar,
        linkGoogle = linkGoogle,
        linkFacebook = linkFacebook,
        linkZalo = linkZalo,
        password = password,
    )
}