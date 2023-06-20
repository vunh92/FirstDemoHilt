package com.vunh.first_demo_hilt.repositories.historyMaintenance

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vunh.first_demo_hilt.models.HistoryMaintenance
import com.vunh.first_demo_hilt.utils.AppUtils
import com.vunh.first_demo_hilt.utils.states.ResultState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class HistoryMaintenanceRepositoryImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcher
) : HistoryMaintenanceRepository {
    override suspend fun getAllHistory(context: Context): Flow<ResultState<MutableList<HistoryMaintenance>>> {
        return flow {
            try {
                val data: String? = AppUtils.getJsonFromAssets(context, "history_maintenance_demo.json")
                val arrayType = object : TypeToken<MutableList<HistoryMaintenance>>(){}.type
//                val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'").create()
                val list: MutableList<HistoryMaintenance>? = Gson().fromJson(data, arrayType)
                if (list.isNullOrEmpty()) {
                    emit(ResultState.Error(message = "List null or empty"))
                } else {
                    emit(ResultState.Success(data = list))
                }
            } catch (e: IOException) {
                emit(ResultState.Error(message = e.message ?: "An error occurred"))
            } catch (e: HttpException) {
                emit(ResultState.Error(message = e.message ?: "An error occurred"))
            }
        }.flowOn(dispatcher)
    }

}