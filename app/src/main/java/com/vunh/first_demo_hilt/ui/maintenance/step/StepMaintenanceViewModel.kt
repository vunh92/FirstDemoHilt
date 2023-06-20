package com.vunh.first_demo_hilt.ui.maintenance.step

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.base.BaseViewModel
import com.vunh.first_demo_hilt.models.Maintenance
import com.vunh.first_demo_hilt.models.Setting
import com.vunh.first_demo_hilt.repositories.mainenance.MaintenanceRepository
import com.vunh.first_demo_hilt.utils.DAY_PER_PERIOD
import com.vunh.first_demo_hilt.utils.DISTANCE_PER_DAY
import com.vunh.first_demo_hilt.utils.extension.*
import com.vunh.first_demo_hilt.utils.states.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class StepMaintenanceViewModel @Inject constructor(
    private val application: Application,
    private val maintenanceRepository: MaintenanceRepository,
) : BaseViewModel() {
    val currentTime = getCalendarDate()
    val setting = MutableLiveData<Setting?>()
    val days = MutableLiveData<Int>()
    val isSuccess = MutableLiveData<Boolean>(false)
    var maintenance: Maintenance? = null

    /* first fragment */
    var typeMaintenanceValue = MutableLiveData<String>()
    var typeMaintenanceData: MutableList<String> = arrayListOf()

    /* second fragment */
    var selectedPeriodMaintenance: Int = 2
    var periodMaintenanceValue = MutableLiveData<Int>()
    var isPeriodMaintenanceCkb: Boolean = true
    var periodMaintenanceData: MutableList<String> = arrayListOf()
    var selectedDistanceMaintenance: Int = 2
    var distanceMaintenanceValue = MutableLiveData<Int>()
    var isDistanceMaintenanceCkb: Boolean = true
    var distanceMaintenanceData: MutableList<String> = arrayListOf()

    /* third fragment */
    var selectedLastTimeMaintenance: Int = -1
    val lastTimeMaintenanceValue = MutableLiveData<Date>()
    var lastTimeMaintenanceData: MutableList<String> = arrayListOf()

    /* last fragment */
    val noteMaintenanceValue = MutableLiveData<String>()
    val remainingKmMaintenance = MutableLiveData<Int>()
    val remainingDateMaintenance = MutableLiveData<Int>()
    val nextMaintenance = MutableLiveData<Date>()

    init {
        typeMaintenanceData = arrayListOf("Thay nhớt máy","Thay nhớt số","Thay lọc gió","Thay lốp","Thay nhông - sên - dĩa")
        periodMaintenanceData = arrayListOf(
            application.getString(R.string.second_maintenance_period_about, "1"),
            application.getString(R.string.second_maintenance_period_about, "2"),
            application.getString(R.string.second_maintenance_period_about, "3"),
            application.getString(R.string.second_maintenance_period_about, "4"),
            application.getString(R.string.second_maintenance_input_period),
        )
        distanceMaintenanceData = arrayListOf(
            application.getString(R.string.second_maintenance_distance_about, "1000"),
            application.getString(R.string.second_maintenance_distance_about, "2000"),
            application.getString(R.string.second_maintenance_distance_about, "3000"),
            application.getString(R.string.second_maintenance_distance_about, "4000"),
            application.getString(R.string.second_maintenance_input_distance),
        )
        lastTimeMaintenanceData = arrayListOf(
            application.getString(R.string.third_maintenance_select_time, "1"),
            application.getString(R.string.third_maintenance_select_time, "2"),
            application.getString(R.string.third_maintenance_select_time, "3"),
            application.getString(R.string.third_maintenance_select_time, "4"),
            application.getString(R.string.third_maintenance_select_other_day),
        )
    }

    fun initMaintenance(maintenance: Maintenance?) {
        this.maintenance = maintenance
        typeMaintenanceValue.value = maintenance?.typeMaintenance ?: typeMaintenanceData.first()
        periodMaintenanceValue.value = maintenance?.periodMaintenance ?: (selectedPeriodMaintenance + 1)
        isPeriodMaintenanceCkb = maintenance?.isPeriodMaintenance ?: true
        distanceMaintenanceValue.value = maintenance?.distanceMaintenance ?: ((selectedDistanceMaintenance + 1)*1000)
        isDistanceMaintenanceCkb = maintenance?.isDistanceMaintenance ?: true
        lastTimeMaintenanceValue.value = maintenance?.lastTimeMaintenance ?: currentTime.time
        noteMaintenanceValue.value = maintenance?.noteMaintenance ?: ""
        calcDays()
    }

    fun getCurrentMaintenance(): Maintenance {
        if (maintenance == null) {
            maintenance = Maintenance(
                userId = profile.value?.id,
                typeMaintenance = typeMaintenanceValue.value,
                periodMaintenance = periodMaintenanceValue.value,
                isPeriodMaintenance = isPeriodMaintenanceCkb,
                distanceMaintenance = distanceMaintenanceValue.value,
                isDistanceMaintenance = isDistanceMaintenanceCkb,
                lastTimeMaintenance = lastTimeMaintenanceValue.value,
                noteMaintenance = noteMaintenanceValue.value,
            )
        }else {
            maintenance!!.apply {
                typeMaintenance = typeMaintenanceValue.value
                periodMaintenance = periodMaintenanceValue.value
                isPeriodMaintenance = isPeriodMaintenanceCkb
                distanceMaintenance = distanceMaintenanceValue.value
                isDistanceMaintenance = isDistanceMaintenanceCkb
                lastTimeMaintenance = lastTimeMaintenanceValue.value
                noteMaintenance = noteMaintenanceValue.value
            }
        }
        return maintenance!!
    }

    fun calcDays() {
        lastTimeMaintenanceValue.value?.let { lastTime ->
            days.value = calcDayBetweenDate(
                startDate = lastTime,
                endDate = currentTime.time,
            )
            calcNextMaintenance()
        }
    }

    private fun calcNextMaintenance() {
        nextMaintenance.value = getDateLater(lastTimeMaintenanceValue.value, periodMaintenanceValue.value!!*DAY_PER_PERIOD)
    }

    fun calcRemainingKm() {
        days.value?.let {
            remainingKmMaintenance.value = distanceMaintenanceValue.value!! - it*DISTANCE_PER_DAY
        }
    }

    fun calcRemainingDate() {
        days.value?.let {
            remainingDateMaintenance.value = periodMaintenanceValue.value!!*DAY_PER_PERIOD - it
            calcNextMaintenance()
        }
    }

    suspend fun insertMaintenance(maintenance: Maintenance) {
        isLoading.value = true
        return withContext(Dispatchers.Main) {
            when (val result = maintenanceRepository.insert(maintenance)) {
                is ResultState.Success -> {
                    isLoading.value = false
                    if (result.data != null) {
                        isSuccess.value = true
                    }
                }
                is ResultState.Error -> {
                    isLoading.value = false
                    isSuccess.value = false
                    showError.value = result.message
                }
            }
        }
    }

    suspend fun updateMaintenance(maintenance: Maintenance) {
        isLoading.value = true
        return withContext(Dispatchers.Main) {
            when (val result = maintenanceRepository.update(maintenance)) {
                is ResultState.Success -> {
                    isLoading.value = false
                    if (result.data != null) {
                        isSuccess.value = true
                    }
                }
                is ResultState.Error -> {
                    isLoading.value = false
                    isSuccess.value = false
                    showError.value = result.message
                }
            }
        }
    }
}