package com.vunh.first_demo_hilt.database

import androidx.room.Dao
import androidx.room.Query
import com.vunh.first_demo_hilt.models.AppDemoModel

@Dao
interface AppDao {
    @Query("SELECT * FROM app_demos ")
    fun getAppDemos(): List<AppDemoModel>
}