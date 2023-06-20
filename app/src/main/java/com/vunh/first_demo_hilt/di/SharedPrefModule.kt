package com.vunh.first_demo_hilt.di

import android.content.Context
import com.vunh.first_demo_hilt.database.AppSharePref
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SharedPrefModule {
    @Provides
    fun provideSharedPref(@ApplicationContext context: Context) : AppSharePref {
        return AppSharePref(context)
    }
}