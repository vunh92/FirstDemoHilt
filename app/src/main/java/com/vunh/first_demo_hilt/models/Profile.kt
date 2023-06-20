package com.vunh.first_demo_hilt.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "tbProfile")
data class Profile(
    @PrimaryKey
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
    val createdCar: Date?,
    @SerializedName("linkGoogle")
    val linkGoogle: Boolean = false,
    @SerializedName("linkFacebook")
    val linkFacebook: Boolean = false,
    @SerializedName("linkZalo")
    val linkZalo: Boolean = false,
    @SerializedName("password")
    val password: String?,
) {

}
