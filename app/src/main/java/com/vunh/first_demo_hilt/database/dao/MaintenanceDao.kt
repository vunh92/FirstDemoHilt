package com.vunh.first_demo_hilt.database.dao

import androidx.room.*
import com.vunh.first_demo_hilt.models.Maintenance

@Dao
interface MaintenanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(maintenance: Maintenance)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(maintenances: MutableList<Maintenance>)

    @Query("DELETE FROM tbMaintenance WHERE id = :id")
    suspend fun delete(id: Int)

    @Update
    suspend fun update(maintenance: Maintenance)

    @Query("SELECT * FROM tbMaintenance LIMIT 1")
    suspend fun getMaintenance(): Maintenance

    @Query("SELECT * FROM tbMaintenance ORDER BY id ASC")
    suspend fun getAll(): MutableList<Maintenance>

    @Query("DELETE FROM tbMaintenance")
    suspend fun clear()
}