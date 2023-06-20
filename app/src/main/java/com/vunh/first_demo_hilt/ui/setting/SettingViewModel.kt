package com.vunh.first_demo_hilt.ui.setting

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vunh.first_demo_hilt.BaseApplication
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.base.BaseViewModel
import com.vunh.first_demo_hilt.models.Prediction
import com.vunh.first_demo_hilt.models.Profile
import com.vunh.first_demo_hilt.models.Setting
import com.vunh.first_demo_hilt.models.User
import com.vunh.first_demo_hilt.repositories.UserRepository
import com.vunh.first_demo_hilt.repositories.profile.ProfileRepository
import com.vunh.first_demo_hilt.repositories.setting.SettingRepository
import com.vunh.first_demo_hilt.utils.states.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val application: Application,
    private val settingRepository: SettingRepository,
) : BaseViewModel() {
    val setting = MutableLiveData<Setting?>()
    val languageSelected = MutableLiveData<Prediction?>()
    var languageList: MutableList<Prediction> = arrayListOf()
    val formatDateSelected = MutableLiveData<Prediction?>()
    var formatDateList: MutableList<Prediction> = arrayListOf()
    val timeRefreshDataSelected = MutableLiveData<Prediction?>()
    var timeRefreshDataList: MutableList<Prediction> = arrayListOf()

    init {
        initLanguage()
        initFormatDate()
        initTimeRefreshData()
        getSetting(1)
    }

    fun getSetting(id: Int) {
        viewModelScope.launch {
            isLoading.value = true
            val result = withContext(Dispatchers.Main){
                settingRepository.getSetting(id = id)
            }
            when (result) {
                is ResultState.Success -> {
                    if (result.data != null) {
                        setting.value = result.data
                        languageList.find { prediction ->  prediction.id == result.data.language }?.let {
                            languageSelected.value = it
                        }
                        formatDateList.find { prediction ->  prediction.id == result.data.formatDate }?.let {
                            formatDateSelected.value = it
                        }
                        timeRefreshDataList.find { prediction ->  prediction.id == result.data.timeRefreshData.toString() }?.let {
                            timeRefreshDataSelected.value = it
                        }
                        isLoading.value = false
                    }
                }
                is ResultState.Error -> {
                    isLoading.value = false
                    Log.e("getSetting", result.message)
                }
            }
        }
    }

    private fun initLanguage() {
        languageList.add(Prediction(id = "vi", name = application.getString(R.string.language_vi), isChecked = false))
        languageList.add(Prediction(id = "en", name = application.getString(R.string.language_en), isChecked = false))
//        languageSelected.value = languageList.first()
    }

    private fun initFormatDate() {
        formatDateList.add(Prediction(id = "about", name = application.getString(R.string.setting_about_time), isChecked = false))
        formatDateList.add(Prediction(id = "time", name = application.getString(R.string.setting_format_time), isChecked = false))
//        formatDateSelected.value = formatDateList.first()
    }

    private fun initTimeRefreshData() {
        timeRefreshDataList.add(Prediction(id = "10", name = application.getString(R.string.setting_time_ten_second), isChecked = false))
        timeRefreshDataList.add(Prediction(id = "20", name = application.getString(R.string.setting_time_twenty_second), isChecked = false))
        timeRefreshDataList.add(Prediction(id = "30", name = application.getString(R.string.setting_time_thirty_second), isChecked = false))
//        timeRefreshDataSelected.value = timeRefreshDataList.first()
    }

    suspend fun updateSetting() {
        return withContext(Dispatchers.Main) {
            isLoading.value = true
//            delay(1000L)
            setting.value?.let {
                when (val result = settingRepository.update(it)) {
                    is ResultState.Success -> {
                        isLoading.value = false
//                        if (result.data != null) {
//                            showToast.value = application.getString(R.string.common_success)
//                        }
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