package com.vunh.first_demo_hilt.database

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppSharePref @Inject constructor(
    @ApplicationContext context: Context
) : Storage {
    companion object {
        private const val PREFERENCE_NAME = "bank_preference"
        private const val PREF_TOKEN = "user_token"
        const val PREF_REMEMBER_ACCOUNT = "remember_account"
        const val PREF_USER_PROFILE = "user_profile"
        const val PREF_IS_DEMO = "is_demo"
    }

    private val sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    override fun saveToken(token: String){
        put(PREF_TOKEN, token)
    }

    override fun getToken() : String {
        return get(PREF_TOKEN, String::class.java)
    }

    override fun clearToken() {
        sharedPreferences.edit().run {
            remove(PREF_TOKEN)
        }.apply()
    }

    override fun setString(key: String, value: String) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            commit()
        }
    }

    override fun getString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun <T> get(key: String, clazz: Class<T>): T =
        when (clazz) {
            String::class.java -> sharedPreferences.getString(key, "")
            Boolean::class.java -> sharedPreferences.getBoolean(key, false)
            Float::class.java -> sharedPreferences.getFloat(key, -1f)
            Double::class.java -> sharedPreferences.getFloat(key, -1f)
            Int::class.java -> sharedPreferences.getInt(key, -1)
            Long::class.java -> sharedPreferences.getLong(key, -1L)
            else -> null
        } as T

    fun <T> put(key: String, data: T) {
        val editor = sharedPreferences.edit()
        when (data) {
            is String -> editor.putString(key, data)
            is Boolean -> editor.putBoolean(key, data)
            is Float -> editor.putFloat(key, data)
            is Double -> editor.putFloat(key, data.toFloat())
            is Int -> editor.putInt(key, data)
            is Long -> editor.putLong(key, data)
        }
        editor.apply()
    }

    override fun clearKey(key: String) {
        sharedPreferences.edit().run {
            remove(key)
        }.apply()
    }

}