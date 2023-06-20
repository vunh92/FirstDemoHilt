package com.vunh.first_demo_hilt.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_demos")
data class AppDemoModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
)
