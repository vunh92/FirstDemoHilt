package com.vunh.first_demo_hilt.ui.support

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.vunh.first_demo_hilt.base.BaseViewModel
import com.vunh.first_demo_hilt.database.AppDatabase
import com.vunh.first_demo_hilt.models.Maintenance
import com.vunh.first_demo_hilt.models.Prediction
import com.vunh.first_demo_hilt.models.Setting
import com.vunh.first_demo_hilt.repositories.mainenance.MaintenanceRepository
import com.vunh.first_demo_hilt.repositories.profile.ProfileRepository
import com.vunh.first_demo_hilt.utils.extension.getCalendarDate
import com.vunh.first_demo_hilt.utils.states.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SupportViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val maintenanceRepository: MaintenanceRepository,
) : BaseViewModel() {
    val currentTime = getCalendarDate()
    val setting = MutableLiveData<Setting?>()
    var maintenanceUserDemo: MutableList<Prediction> = Prediction.getListDemo()
    val maintenanceFilterList = MutableLiveData<MutableList<Maintenance>>()

    init {
        viewModelScope.launch {
            getProfile(profileRepository)
            getMaintenanceList()
        }
    }

    suspend fun getMaintenanceList() {
        return withContext(Dispatchers.Main) {
            isLoading.value = true
            when (val result = maintenanceRepository.getMaintenanceList()) {
                is ResultState.Success -> {
                    isLoading.value = false
                    result.data?.let {
                        maintenanceFilterList.value = it
                    }
                }
                is ResultState.Error -> {
                    isLoading.value = false
                    showError.value = result.message
                }
            }
        }

    }

    suspend fun updateMaintenance(maintenance: Maintenance?) {
        return withContext(Dispatchers.Main) {
            isLoading.value = true
            maintenance?.let {
                when (val result = maintenanceRepository.update(maintenance)) {
                    is ResultState.Success -> {
                        isLoading.value = false
                        result.data?.let {
                            val maintenanceList = maintenanceFilterList.value?.map {
                                if(it.id == maintenance.id) {
                                    result.data
                                }
                                else {
                                    it
                                }
                            }
                            maintenanceFilterList.value = maintenanceList?.toMutableList()
//                            maintenanceList?.let {
//                                maintenanceFilterList.value = it
//                            }
                        }
                    }
                    is ResultState.Error -> {
                        isLoading.value = false
                        showError.value = result.message
                    }
                }
            }
        }
    }

    suspend fun deleteMaintenance(itemDelete: Maintenance) {
        return withContext(Dispatchers.Main) {
            isLoading.value = true
            when (val result = maintenanceRepository.delete(itemDelete.id)) {
                is ResultState.Success -> {
                    isLoading.value = false
                    val maintenanceList = maintenanceFilterList.value?.map {
                        if(it.id == itemDelete.id) {
//                            it.copy(id = itemDelete.id, isDelete = true)
                            val newItem = it.copy(isDelete = true)
                            newItem.id = itemDelete.id
                            newItem
                        }
                        else {
                            it
                        }
                    }?.toMutableList()
                    maintenanceFilterList.value = maintenanceList?.filter { !it.isDelete }?.toMutableList()
                }
                is ResultState.Error -> {
                    isLoading.value = false
                    showError.value = result.message
                }
            }
        }
    }

}