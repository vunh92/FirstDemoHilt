package com.vunh.first_demo_hilt.database.dao

import androidx.room.*
import com.vunh.first_demo_hilt.models.Profile

@Dao
interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: Profile)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(profiles: MutableList<Profile>)

    @Query("DELETE FROM tbProfile WHERE id = :id")
    suspend fun delete(id: Int)

    @Update
    suspend fun update(profile: Profile)

    @Query("SELECT * FROM tbProfile LIMIT 1")
    suspend fun getProfile(): Profile

    @Query("SELECT * FROM tbProfile ")
    fun getAll(): MutableList<Profile>

    @Query("DELETE FROM tbProfile")
    suspend fun clear()
}