package com.vunh.first_demo_hilt.repositories.mainenance

import com.vunh.first_demo_hilt.database.AppDatabase
import com.vunh.first_demo_hilt.models.Maintenance
import com.vunh.first_demo_hilt.utils.states.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MaintenanceRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
): MaintenanceRepository {
    override suspend fun insert(maintenance: Maintenance): ResultState<Maintenance> {
        try {
            val status = withContext(Dispatchers.Main) {
                appDatabase.maintenanceDao().insert(maintenance)
            }
            status.let {
                return ResultState.Success(data = maintenance)
            }
        }catch (ex: Throwable) {
            return ResultState.Error(ex.message ?: "")
        }
    }

    override suspend fun delete(id: Int): ResultState<Int> {
        try {
            val status = withContext(Dispatchers.Main) {
                appDatabase.maintenanceDao().delete(id)
            }
            status.let {
                if (id < 0) return ResultState.Error(id.toString())
                return ResultState.Success(data = id)
            }
        }catch (ex: Throwable) {
            return ResultState.Error(ex.message ?: "")
        }
    }

    override suspend fun update(maintenance: Maintenance): ResultState<Maintenance> {
        try {
            val status = withContext(Dispatchers.Main) {
                appDatabase.maintenanceDao().update(maintenance)
            }
            status.let {
                return ResultState.Success(data = maintenance)
            }
        }catch (ex: Throwable) {
            return ResultState.Error(ex.message ?: "")
        }
    }

    override suspend fun getMaintenance(): ResultState<Maintenance> {
        try {
            val status = withContext(Dispatchers.Main) {
                appDatabase.maintenanceDao().getMaintenance()
            }
            status.let {
                return ResultState.Success(data = status)
            }
        }catch (ex: Throwable) {
            return ResultState.Error(ex.message ?: "")
        }
    }

    override suspend fun getMaintenanceList(): ResultState<MutableList<Maintenance>> {
        try {
            val status = withContext(Dispatchers.Main) {
                appDatabase.maintenanceDao().getAll()
            }
            status.let {
                return ResultState.Success(data = status)
            }
        }catch (ex: Throwable) {
            return ResultState.Error(ex.message ?: "")
        }
    }

    override suspend fun clear(): ResultState<Boolean> {
        try {
            val status = withContext(Dispatchers.Main) {
                appDatabase.maintenanceDao().clear()
            }
            status.let {
                return ResultState.Success(data = true)
            }
        }catch (ex: Throwable) {
            return ResultState.Error(ex.message ?: "")
        }
    }
}