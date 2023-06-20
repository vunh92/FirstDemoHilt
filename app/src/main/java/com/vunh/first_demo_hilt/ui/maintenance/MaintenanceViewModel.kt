package com.vunh.first_demo_hilt.ui.maintenance

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
class MaintenanceViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val maintenanceRepository: MaintenanceRepository,
) : BaseViewModel() {
    val currentTime = getCalendarDate()
    val setting = MutableLiveData<Setting?>()
    var maintenanceUserDemo: MutableList<Prediction> = Prediction.getListDemo()
    val maintenanceFilterList = MutableLiveData<MutableList<Maintenance>>(arrayListOf())

    init {
        viewModelScope.launch {
            getProfile(profileRepository)
            getMaintenanceList()
        }
    }

    fun maintenanceFilter(list: MutableList<Maintenance>?) {
        val filterList = arrayListOf<Maintenance>()
        maintenanceUserDemo.forEach { user ->
            if (user.isChecked) {
                list?.filter { it.userId.toString() == user.id && !it.isDelete}?.let { filterList.addAll(it) }
            }
        }
        maintenanceFilterList.value = filterList
    }

    suspend fun getMaintenanceList() {
        return withContext(Dispatchers.Main) {
            isLoading.value = true
            when (val result = maintenanceRepository.getMaintenanceList()) {
                is ResultState.Success -> {
                    isLoading.value = false
                    result.data?.let {
                        maintenanceFilter(it)
                    }
                }
                is ResultState.Error -> {
                    isLoading.value = false
                    showError.value = result.message
                }
            }
        }
    }

    suspend fun updateMaintenance(maintenance: Maintenance) {
        return withContext(Dispatchers.Main) {
            isLoading.value = true
            when (val result = maintenanceRepository.update(maintenance)) {
                is ResultState.Success -> {
                    isLoading.value = false
                    result.data?.let {
                        val maintenanceList = maintenanceFilterList.value?.map {
                            if(it.id == maintenance.id) result.data
                            else it
                        }?.toMutableList()
                        maintenanceFilter(maintenanceList)
                    }
                }
                is ResultState.Error -> {
                    isLoading.value = false
                    showError.value = result.message
                }
            }
        }
    }

    suspend fun deleteMaintenance(itemDelete: Maintenance) {
        return withContext(Dispatchers.Main) {
            isLoading.value = true
            itemDelete.isDelete = true
            when (val result = maintenanceRepository.update(itemDelete)) {
                is ResultState.Success -> {
                    isLoading.value = false
                    val maintenanceList = maintenanceFilterList.value?.map {
                        if(it.id == itemDelete.id) itemDelete else it
                    }?.toMutableList()
                    maintenanceFilter(maintenanceList)
//                    maintenanceFilterList.value = maintenanceList?.filter { !it.isDelete }?.toMutableList()
                }
                is ResultState.Error -> {
                    isLoading.value = false
                    showError.value = result.message
                }
            }
        }
    }

}