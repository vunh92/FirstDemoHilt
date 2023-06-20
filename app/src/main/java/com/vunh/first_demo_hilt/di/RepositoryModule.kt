package com.vunh.first_demo_hilt.di

import com.vunh.first_demo_hilt.database.AppDatabase
import com.vunh.first_demo_hilt.network.ApiReqService
import com.vunh.first_demo_hilt.repositories.UserRepository
import com.vunh.first_demo_hilt.repositories.UserRepositoryImpl
import com.vunh.first_demo_hilt.repositories.historyMaintenance.HistoryMaintenanceRepository
import com.vunh.first_demo_hilt.repositories.historyMaintenance.HistoryMaintenanceRepositoryImpl
import com.vunh.first_demo_hilt.repositories.login.LoginRepository
import com.vunh.first_demo_hilt.repositories.login.LoginRepositoryImpl
import com.vunh.first_demo_hilt.repositories.mainenance.MaintenanceRepository
import com.vunh.first_demo_hilt.repositories.mainenance.MaintenanceRepositoryImpl
import com.vunh.first_demo_hilt.repositories.news.NewsRepository
import com.vunh.first_demo_hilt.repositories.news.NewsRepositoryImpl
import com.vunh.first_demo_hilt.repositories.profile.ProfileRepository
import com.vunh.first_demo_hilt.repositories.profile.ProfileRepositoryImpl
import com.vunh.first_demo_hilt.repositories.setting.SettingRepository
import com.vunh.first_demo_hilt.repositories.setting.SettingRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
class RepositoryModule {

    @[Provides Singleton]
    fun provideUserRepository(
        apiReqService: ApiReqService,
        ioDispatcher: CoroutineDispatcher
    ): UserRepository = UserRepositoryImpl(apiService = apiReqService, dispatcher = ioDispatcher)

    @[Provides Singleton]
    fun provideLoginRepository(
        apiReqService: ApiReqService,
        ioDispatcher: CoroutineDispatcher
    ): LoginRepository = LoginRepositoryImpl(apiService = apiReqService, dispatcher = ioDispatcher)

    @[Provides Singleton]
    fun provideProfileRepository(
        appDatabase: AppDatabase
    ): ProfileRepository = ProfileRepositoryImpl(appDatabase = appDatabase)

    @[Provides Singleton]
    fun provideSettingRepository(
        appDatabase: AppDatabase
    ): SettingRepository = SettingRepositoryImpl(appDatabase = appDatabase)

    @[Provides Singleton]
    fun provideMaintenanceRepository(
        appDatabase: AppDatabase
    ): MaintenanceRepository = MaintenanceRepositoryImpl(appDatabase = appDatabase)

    @[Provides Singleton]
    fun provideNewsRepository(
        ioDispatcher: CoroutineDispatcher
    ): NewsRepository = NewsRepositoryImpl(dispatcher = ioDispatcher)

    @[Provides Singleton]
    fun provideHistoryMaintenanceRepository(
        ioDispatcher: CoroutineDispatcher
    ): HistoryMaintenanceRepository = HistoryMaintenanceRepositoryImpl(dispatcher = ioDispatcher)

}