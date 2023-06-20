package com.vunh.first_demo_hilt.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.vunh.first_demo_hilt.models.Profile
import com.vunh.first_demo_hilt.repositories.profile.ProfileRepository
import com.vunh.first_demo_hilt.repositories.setting.SettingRepository
import com.vunh.first_demo_hilt.utils.states.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
open class BaseViewModel @Inject constructor(): ViewModel() , CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    val isLoading = MutableLiveData<Boolean>()
    val showError = MutableLiveData<String?>()
    val showSuccess = MutableLiveData<String?>()
    val showToast = MutableLiveData<String?>()
    val profile = MutableLiveData<Profile?>()

    fun getProfile(profileRepository: ProfileRepository) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.Main){
                profileRepository.getProfile()
            }
            when (result) {
                is ResultState.Success -> {
                    if (result.data != null) {
                        profile.value = result.data
                    }
                }
                is ResultState.Error -> {
                    Log.e("getProfile", result.message)
                }
            }
        }
    }

    suspend fun insertProfile(profile: Profile, profileRepository: ProfileRepository) {
        return withContext(Dispatchers.Main) {
            when (val result = profileRepository.insert(profile)) {
                is ResultState.Success -> {
                    if (result.data != null) {
                        Log.i("insertProfile", Gson().toJson(result.data))
                    }
                }
                is ResultState.Error -> {
                    Log.e("insertProfile", result.message)
                }
            }
        }
    }

    suspend fun deleteProfile(profileId: Int, profileRepository: ProfileRepository) {
        return withContext(Dispatchers.Main) {
            when (val result = profileRepository.delete(profileId)) {
                is ResultState.Success -> {
                    if (result.data != null) {
                        Log.i("deleteProfile", result.data.toString())
                    }
                }
                is ResultState.Error -> {
                    Log.e("deleteProfile", result.message)
                }
            }
        }
    }

    suspend fun clearProfile(profileRepository: ProfileRepository) {
        return withContext(Dispatchers.Main) {
            when (val result = profileRepository.clear()) {
                is ResultState.Success -> {
                    if (result.data != null) {
                        Log.i("clearProfile", result.data.toString())
                    }
                }
                is ResultState.Error -> {
                    Log.e("clearProfile", result.message)
                }
            }
        }
    }

    suspend fun clearSetting(settingRepository: SettingRepository) {
        return withContext(Dispatchers.Main) {
            when (val result = settingRepository.clear()) {
                is ResultState.Success -> {
                    if (result.data != null) {
                        Log.i("clearSetting", result.data.toString())
                    }
                }
                is ResultState.Error -> {
                    Log.e("clearSetting", result.message)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}