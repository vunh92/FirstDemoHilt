package com.vunh.first_demo_hilt.database

interface Storage {
    fun setString(key: String, value: String)
    fun getString(key: String): String?
    fun saveToken(key: String)
    fun getToken(): String?
    fun clearToken()
    fun clearKey(key: String)
}