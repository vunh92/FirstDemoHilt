package com.vunh.first_demo_hilt.database.dao

import androidx.room.*
import com.vunh.first_demo_hilt.models.Setting

@Dao
interface SettingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(setting: Setting)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(settings: MutableList<Setting>)

    @Query("DELETE FROM tbSetting WHERE id = :id")
    suspend fun delete(id: Int)

    @Update
    suspend fun update(setting: Setting)

    @Query("SELECT * FROM tbSetting WHERE id = :id")
    suspend fun get(id: Int): Setting

    @Query("SELECT * FROM tbSetting ")
    fun getAll(): MutableList<Setting>

    @Query("DELETE FROM tbSetting")
    suspend fun clear()
}