package com.vunh.first_demo_hilt.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.vunh.first_demo_hilt.base.BaseViewModel
import com.vunh.first_demo_hilt.models.Profile
import com.vunh.first_demo_hilt.models.Setting
import com.vunh.first_demo_hilt.models.User
import com.vunh.first_demo_hilt.models.dto.LoginRequest
import com.vunh.first_demo_hilt.repositories.login.LoginRepository
import com.vunh.first_demo_hilt.repositories.profile.ProfileRepository
import com.vunh.first_demo_hilt.repositories.setting.SettingRepository
import com.vunh.first_demo_hilt.ui.home.UserProfileState
import com.vunh.first_demo_hilt.utils.WrappedResponse
import com.vunh.first_demo_hilt.utils.states.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val profileRepository: ProfileRepository,
    private val settingRepository: SettingRepository,
): BaseViewModel() {
    private val _loginState = MutableStateFlow<LoginActivityState>(LoginActivityState.Init)
    val loginState: StateFlow<LoginActivityState> get() = _loginState

    private val _loginDemoState = MutableStateFlow<LoginActivityState>(LoginActivityState.Init)
    val loginDemoState: StateFlow<LoginActivityState> get() = _loginDemoState

    private fun setLoading(){
        _loginState.value = LoginActivityState.IsLoading(true)
    }

    private fun hideLoading(){
        _loginState.value = LoginActivityState.IsLoading(false)
    }

    private fun showToast(message: String){
        _loginState.value = LoginActivityState.ShowToast(message)
    }

    init {
        getProfile(profileRepository)
    }

    fun login(loginRequest: LoginRequest){
        viewModelScope.launch {
            loginRepository.login(loginRequest = loginRequest)
                .onStart {
                    setLoading()
                }
                .catch {
                    hideLoading()
                    showToast(it.message.toString())
                }
                .collect {
                    hideLoading()
                    when(it) {
                        is ResultState.Error -> {
                            _loginState.value = LoginActivityState.ErrorLogin(message = it.message)
                        }
                        is ResultState.Success -> {
                            _loginState.value = LoginActivityState.SuccessLogin(user = it.data?.data)
                        }
                    }
                }
        }
    }

    fun loginDemo() {
        viewModelScope.launch {
            loginRepository.loginDemo()
                .onStart {
                    setLoading()
                }
                .catch {
                    hideLoading()
                    showToast(it.message.toString())
                }
                .collect {
                    hideLoading()
                    when(it) {
                        is ResultState.Error -> {
                            _loginState.value = LoginActivityState.ErrorLogin(message = it.message)
                        }
                        is ResultState.Success -> {
                            _loginState.value = LoginActivityState.SuccessLogin(user = it.data?.data)
                        }
                    }
                }
        }
    }

    suspend fun saveProfile(profile: Profile) = insertProfile(profile, profileRepository)

    suspend fun saveSetting(setting: Setting) {
        return withContext(Dispatchers.Main) {
            when (val result = settingRepository.insert(setting)) {
                is ResultState.Success -> {
                    if (result.data != null) {
                        Log.e("saveSetting", Gson().toJson(result.data))
                    }
                }
                is ResultState.Error -> {
                    Log.e("saveSetting", result.message)
                }
            }
        }
    }

//    fun login(loginRequest: LoginRequest){
//        viewModelScope.launch {
//            loginUseCase.invoke(loginRequest)
//                .onStart {
//                    setLoading()
//                }
//                .catch { exception ->
//                    hideLoading()
//                    showToast(exception.message.toString())
//                }
//                .collect { baseResult ->
//                    hideLoading()
//                    when(baseResult){
//                        is BaseResult.Error -> state.value = LoginActivityState.ErrorLogin(baseResult.rawResponse)
//                        is BaseResult.Success -> state.value = LoginActivityState.SuccessLogin(baseResult.data)
//                    }
//                }
//        }
//    }
}

