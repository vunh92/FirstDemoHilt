package com.vunh.first_demo_hilt.ui.notification

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.base.BaseViewModel
import com.vunh.first_demo_hilt.models.Prediction
import com.vunh.first_demo_hilt.models.Setting
import com.vunh.first_demo_hilt.repositories.setting.SettingRepository
import com.vunh.first_demo_hilt.utils.states.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val application: Application,
    private val settingRepository: SettingRepository,
) : BaseViewModel() {
    val setting = MutableLiveData<Setting?>()
    var notificationListDemo: MutableList<Prediction> = Prediction.getListDemo()

    init {
        getSetting(1)
    }

    fun getSetting(id: Int) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.Main){
                settingRepository.getSetting(id = id)
            }
            when (result) {
                is ResultState.Success -> {
                    if (result.data != null) {
                        setting.value = result.data
                    }
                }
                is ResultState.Error -> {
                    Log.e("getSetting", result.message)
                }
            }
        }
    }

    suspend fun updateSettingNotification() {
        return withContext(Dispatchers.Main) {
            isLoading.value = true
            setting.value?.let {
                when (val result = settingRepository.update(it)) {
                    is ResultState.Success -> {
                        isLoading.value = false
                        if (result.data != null) {
                            showSuccess.value = application.getString(R.string.common_success)
                        }
                    }
                    is ResultState.Error -> {
                        isLoading.value = false
                        showError.value = result.message
                    }
                }
            } ?: run {
                isLoading.value = false
                showError.value = application.getString(R.string.common_update_fail)
            }
        }
    }

}