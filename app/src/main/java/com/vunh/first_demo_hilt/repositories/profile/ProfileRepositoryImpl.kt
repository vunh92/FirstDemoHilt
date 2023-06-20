package com.vunh.first_demo_hilt.repositories.profile

import com.vunh.first_demo_hilt.database.AppDatabase
import com.vunh.first_demo_hilt.models.Profile
import com.vunh.first_demo_hilt.utils.states.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
): ProfileRepository {
    override suspend fun insert(profile: Profile): ResultState<Profile> {
        try {
            val status = withContext(Dispatchers.Main) {
                appDatabase.profileDao().insert(profile)
            }
            status.let {
                return ResultState.Success(data = profile)
            }
        }catch (ex: Throwable) {
            return ResultState.Error(ex.message ?: "")
        }
    }

    override suspend fun delete(id: Int): ResultState<Int> {
        try {
            val status = withContext(Dispatchers.Main) {
                appDatabase.profileDao().delete(id)
            }
            status.let {
                return ResultState.Success(data = id)
            }
        }catch (ex: Throwable) {
            return ResultState.Error(ex.message ?: "")
        }
    }

    override suspend fun update(profile: Profile): ResultState<Profile> {
        try {
            val status = withContext(Dispatchers.Main) {
                appDatabase.profileDao().update(profile)
            }
            status.let {
                return ResultState.Success(data = profile)
            }
        }catch (ex: Throwable) {
            return ResultState.Error(ex.message ?: "")
        }
    }

    override suspend fun getProfile(): ResultState<Profile> {
        try {
            val status = withContext(Dispatchers.Main) {
                appDatabase.profileDao().getProfile()
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
                appDatabase.profileDao().clear()
            }
            status.let {
                return ResultState.Success(data = true)
            }
        }catch (ex: Throwable) {
            return ResultState.Error(ex.message ?: "")
        }
    }
}