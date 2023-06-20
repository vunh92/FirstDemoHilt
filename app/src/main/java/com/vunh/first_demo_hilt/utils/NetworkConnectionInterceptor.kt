package com.vunh.first_demo_hilt.utils

import android.content.Context
import com.vunh.first_demo_hilt.database.AppSharePref
import com.vunh.first_demo_hilt.utils.AppUtils.isNetworkAvailable
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Oguz Sahin on 11/10/2021.
 */

class NetworkConnectionInterceptor @Inject constructor(private val context: Context, private val pref: AppSharePref) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isNetworkAvailable(context)) {
            throw NoConnectionException()
        }
        val token = pref.getToken()
        val builder: Request.Builder = chain.request().newBuilder()
        builder.addHeader("Authorization", token)
        return chain.proceed(builder.build())
    }

    inner class NoConnectionException : IOException() {
        override val message: String
            get() = super.message ?: "No Internet Connection"
    }
}