package com.vunh.first_demo_hilt.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vunh.first_demo_hilt.database.dao.MaintenanceDao
import com.vunh.first_demo_hilt.database.dao.ProfileDao
import com.vunh.first_demo_hilt.database.dao.SettingDao
import com.vunh.first_demo_hilt.models.*

@Database(entities = [AppDemoModel::class, Profile::class, Setting::class, Maintenance::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
    abstract fun profileDao(): ProfileDao
    abstract fun settingDao(): SettingDao
    abstract fun maintenanceDao(): MaintenanceDao
}