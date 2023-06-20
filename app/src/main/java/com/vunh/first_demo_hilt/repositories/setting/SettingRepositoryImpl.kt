package com.vunh.first_demo_hilt.repositories.setting

import com.vunh.first_demo_hilt.database.AppDatabase
import com.vunh.first_demo_hilt.models.Profile
import com.vunh.first_demo_hilt.models.Setting
import com.vunh.first_demo_hilt.utils.states.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
): SettingRepository {
    override suspend fun insert(setting: Setting): ResultState<Setting> {
        try {
            val status = withContext(Dispatchers.Main) {
                appDatabase.settingDao().insert(setting)
            }
            status.let {
                return ResultState.Success(data = setting)
            }
        }catch (ex: Throwable) {
            return ResultState.Error(ex.message ?: "")
        }
    }

    override suspend fun delete(id: Int): ResultState<Int> {
        try {
            val status = withContext(Dispatchers.Main) {
                appDatabase.settingDao().delete(id)
            }
            status.let {
                return ResultState.Success(data = id)
            }
        }catch (ex: Throwable) {
            return ResultState.Error(ex.message ?: "")
        }
    }

    override suspend fun update(setting: Setting): ResultState<Setting> {
        try {
            val status = withContext(Dispatchers.Main) {
                appDatabase.settingDao().update(setting)
            }
            status.let {
                return ResultState.Success(data = setting)
            }
        }catch (ex: Throwable) {
            return ResultState.Error(ex.message ?: "")
        }
    }

    override suspend fun getSetting(id: Int): ResultState<Setting> {
        try {
            val status = withContext(Dispatchers.Main) {
                appDatabase.settingDao().get(id = id)
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
                appDatabase.settingDao().clear()
            }
            status.let {
                return ResultState.Success(data = true)
            }
        }catch (ex: Throwable) {
            return ResultState.Error(ex.message ?: "")
        }
    }
}