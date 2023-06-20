package com.vunh.first_demo_hilt.di

import androidx.room.Room
import com.vunh.first_demo_hilt.BaseApplication
import com.vunh.first_demo_hilt.database.AppDao
import com.vunh.first_demo_hilt.database.AppDatabase
import com.vunh.first_demo_hilt.database.dao.MaintenanceDao
import com.vunh.first_demo_hilt.database.dao.ProfileDao
import com.vunh.first_demo_hilt.database.dao.SettingDao
import com.vunh.first_demo_hilt.utils.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
class DatabaseModule {

    @[Singleton Provides]
    fun provideDatabase(app: BaseApplication): AppDatabase =
        Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

    @[Singleton Provides]
    fun provideAppDao(db: AppDatabase): AppDao = db.appDao()

    @[Singleton Provides]
    fun provideProfileDao(db: AppDatabase): ProfileDao = db.profileDao()

    @[Singleton Provides]
    fun provideSettingDao(db: AppDatabase): SettingDao = db.settingDao()

    @[Singleton Provides]
    fun provideMaintenanceDao(db: AppDatabase): MaintenanceDao = db.maintenanceDao()

}