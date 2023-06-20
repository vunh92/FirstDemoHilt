package com.vunh.first_demo_hilt.di

import android.content.Context
import com.vunh.first_demo_hilt.network.ApiReqService
import com.vunh.first_demo_hilt.database.AppSharePref
import com.vunh.first_demo_hilt.utils.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier annotation class AppRetrofit
@Qualifier annotation class ListUserRetrofit
@Qualifier annotation class ReqRetrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    @AppRetrofit
    fun provideAppRetrofit(okHttpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder().apply {
            addConverterFactory(GsonConverterFactory.create())
            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            client(okHttpClient)
            baseUrl(BASE_URL)
        }.build()
    }

    @Singleton
    @Provides
    @ReqRetrofit
    fun providerReqRetrofit(okHttpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder().apply {
            addConverterFactory(GsonConverterFactory.create())
            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            client(okHttpClient)
            baseUrl(REQRES_URL)
        }.build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        networkConnectionInterceptor: NetworkConnectionInterceptor
    ): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        return OkHttpClient.Builder().apply {
//            addNetworkInterceptor(httpLoggingInterceptor)
            addInterceptor(httpLoggingInterceptor)
            addInterceptor(networkConnectionInterceptor)
            connectTimeout(TIMEOUT_CONNECT, TimeUnit.SECONDS)
            readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
            writeTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
        }.build()
    }

    @Provides
    fun provideNetworkConnectionInterceptor(@ApplicationContext context: Context, prefs: AppSharePref) : NetworkConnectionInterceptor {
        return NetworkConnectionInterceptor(context, prefs)
    }

    @Singleton
    @Provides
    fun provideApiReqService(
        @ReqRetrofit retrofit: Retrofit
    ): ApiReqService = retrofit.create(ApiReqService::class.java)
}
