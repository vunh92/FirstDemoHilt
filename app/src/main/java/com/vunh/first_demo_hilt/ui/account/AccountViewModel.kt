package com.vunh.first_demo_hilt.ui.account

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vunh.first_demo_hilt.BaseApplication
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.base.BaseViewModel
import com.vunh.first_demo_hilt.models.Profile
import com.vunh.first_demo_hilt.models.User
import com.vunh.first_demo_hilt.repositories.UserRepository
import com.vunh.first_demo_hilt.repositories.profile.ProfileRepository
import com.vunh.first_demo_hilt.utils.states.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val application: BaseApplication,
    private val profileRepository: ProfileRepository,
    private val dispatcher: CoroutineDispatcher,
) : BaseViewModel() {
    val updatePasswordState = MutableLiveData<ChangePasswordState>(ChangePasswordState.Init)
    val updateInfoState = MutableLiveData<UpdateInfoState>(UpdateInfoState.Init)

    init {
        getProfile(profileRepository)
    }

    fun getProfile() = getProfile(profileRepository)

    suspend fun deleteProfile(profileId: Int) {
        return deleteProfile(profileId, profileRepository)
    }

    suspend fun clearProfile() = clearProfile(profileRepository)

    suspend fun updatePassword(oldPassword: String, newPassword: String) {
        return withContext(Dispatchers.Main) {
            isLoading.value = true
            delay(1000L)
            profile.value?.let {
                if (oldPassword != it.password) {
                    updatePasswordState.value = ChangePasswordState.Error(message = application.getString(R.string.common_update_fail))
                    return@let
                }
                val newProfile = it.copy(password = newPassword)
                when (val result = profileRepository.update(newProfile)) {
                    is ResultState.Success -> {
                        if (result.data != null) {
                            profile.value = result.data
                            updatePasswordState.value = ChangePasswordState.Success(result = true)
                        }
                    }
                    is ResultState.Error -> {
                        updatePasswordState.value = ChangePasswordState.Error(message = result.message)
                    }
                }
            } ?: run { updatePasswordState.value = ChangePasswordState.Error(message = application.getString(R.string.common_update_fail)) }
        }
    }

    suspend fun updateInfo(username: String, email: String, address: String) {
        return withContext(Dispatchers.Main) {
            isLoading.value = true
            delay(1000L)
            profile.value?.let {
                val newProfile = it.copy(
                    firstName = username,
                    email = email,
                    address = address,
                )
                when (val result = profileRepository.update(newProfile)) {
                    is ResultState.Success -> {
                        if (result.data != null) {
                            profile.value = result.data
                            updateInfoState.value = UpdateInfoState.Success(result = true)
                        }
                    }
                    is ResultState.Error -> {
                        updateInfoState.value = UpdateInfoState.Error(message = result.message)
                    }
                }
            } ?: run { updateInfoState.value = UpdateInfoState.Error(message = application.getString(R.string.common_update_fail)) }
        }
    }

    fun updateLinkGoogle(isChecked: Boolean) {
        viewModelScope.launch {
            profile.value?.let {
                val newProfile = it.copy(linkGoogle = isChecked)
                when (val result = profileRepository.update(newProfile)) {
                    is ResultState.Success -> {
                        if (result.data != null) {
                            profile.value = result.data
                        }
                    }
                    is ResultState.Error -> {
                        updateInfoState.value = UpdateInfoState.Error(message = result.message)
                    }
                }
            } ?: run { updateInfoState.value = UpdateInfoState.Error(message = application.getString(R.string.common_update_fail)) }
        }
    }

    fun updateLinkFacebook(isChecked: Boolean) {
        viewModelScope.launch {
            profile.value?.let {
                val newProfile = it.copy(linkFacebook = isChecked)
                when (val result = profileRepository.update(newProfile)) {
                    is ResultState.Success -> {
                        if (result.data != null) {
                            profile.value = result.data
                        }
                    }
                    is ResultState.Error -> {
                        updateInfoState.value = UpdateInfoState.Error(message = result.message)
                    }
                }
            } ?: run { updateInfoState.value = UpdateInfoState.Error(message = application.getString(R.string.common_update_fail)) }
        }
    }

    fun updateLinkZalo(isChecked: Boolean) {
        viewModelScope.launch {
            profile.value?.let {
                val newProfile = it.copy(linkZalo = isChecked)
                when (val result = profileRepository.update(newProfile)) {
                    is ResultState.Success -> {
                        if (result.data != null) {
                            profile.value = result.data
                        }
                    }
                    is ResultState.Error -> {
                        updateInfoState.value = UpdateInfoState.Error(message = result.message)
                    }
                }
            } ?: run { updateInfoState.value = UpdateInfoState.Error(message = application.getString(R.string.common_update_fail)) }
        }
    }
}