package com.vunh.first_demo_hilt.repositories.mainenance

import com.vunh.first_demo_hilt.models.Maintenance
import com.vunh.first_demo_hilt.utils.states.ResultState

interface MaintenanceRepository {
    suspend fun insert(maintenance: Maintenance) : ResultState<Maintenance>
    suspend fun delete(id: Int) : ResultState<Int>
    suspend fun update(maintenance: Maintenance) : ResultState<Maintenance>
    suspend fun clear() : ResultState<Boolean>
    suspend fun getMaintenance() : ResultState<Maintenance>
    suspend fun getMaintenanceList() : ResultState<MutableList<Maintenance>>
}