package com.vunh.first_demo_hilt.di

import android.content.Context
import com.vunh.first_demo_hilt.BaseApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
object AppModule {

    @[Singleton Provides]
    fun provideApplication(
        @ApplicationContext app: Context
    ): BaseApplication = app as BaseApplication

    @[Singleton Provides]
    fun provideDispatcher(): CoroutineDispatcher =
        Dispatchers.IO
}