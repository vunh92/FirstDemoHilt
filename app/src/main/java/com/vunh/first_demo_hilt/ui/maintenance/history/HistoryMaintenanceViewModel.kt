package com.vunh.first_demo_hilt.ui.maintenance.history

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vunh.first_demo_hilt.base.BaseViewModel
import com.vunh.first_demo_hilt.models.HistoryMaintenance
import com.vunh.first_demo_hilt.models.Maintenance
import com.vunh.first_demo_hilt.repositories.historyMaintenance.HistoryMaintenanceRepository
import com.vunh.first_demo_hilt.repositories.profile.ProfileRepository
import com.vunh.first_demo_hilt.utils.extension.getToday
import com.vunh.first_demo_hilt.utils.extension.toStringDate
import com.vunh.first_demo_hilt.utils.states.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryMaintenanceViewModel @Inject constructor(
    private val application: Application,
    private val profileRepository: ProfileRepository,
    private val historyMaintenanceRepository: HistoryMaintenanceRepository,
) : BaseViewModel() {
    val currentTime = getToday()
    var maintenance: Maintenance? = null
    val itemList = MutableLiveData<MutableList<String>>(arrayListOf())

    init {
//        getProfile(profileRepository)
    }

    fun getHistoryMaintenanceList() {
        viewModelScope.launch {
            isLoading.value = true
            delay(1000L)
            historyMaintenanceRepository.getAllHistory(context = application)
                .onStart {
//                    isLoading.value = true
                }
                .catch {
                    isLoading.value = false
                    showError.value = it.message.toString()
                }
                .collect {
                    isLoading.value = false
                    when(it) {
                        is ResultState.Error -> {
                            showError.value = it.message
                        }
                        is ResultState.Success -> {
                            if (it.data != null) {
                                filterList(it.data)
                            }
                        }
                    }
                }
        }
    }

    private fun filterList(list: MutableList<HistoryMaintenance>) {
        profile.value?.let { profile ->
            val newList = list.filter { it.userId == profile.id }.map { it.timeMaintenance.toStringDate() ?: "" }.toMutableList()
            newList.add(profile.createdCar.toStringDate() ?: "")
            itemList.value = newList
        }
    }
}