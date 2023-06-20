package com.vunh.first_demo_hilt.repositories.historyMaintenance

import android.content.Context
import com.vunh.first_demo_hilt.models.HistoryMaintenance
import com.vunh.first_demo_hilt.utils.states.ResultState
import kotlinx.coroutines.flow.Flow

interface HistoryMaintenanceRepository {
    suspend fun getAllHistory(context: Context) : Flow<ResultState<MutableList<HistoryMaintenance>>>
}