package com.vunh.first_demo_hilt.repositories.login

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vunh.first_demo_hilt.models.User
import com.vunh.first_demo_hilt.models.dto.LoginRequest
import com.vunh.first_demo_hilt.network.ApiReqService
import com.vunh.first_demo_hilt.utils.WrappedResponse
import com.vunh.first_demo_hilt.utils.states.DataState
import com.vunh.first_demo_hilt.utils.states.ResultState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val apiService: ApiReqService,
    private val dispatcher: CoroutineDispatcher
) : LoginRepository {
    override suspend fun login(loginRequest: LoginRequest): Flow<ResultState<WrappedResponse<User>>> {
        return flow {
            try {
                val response = apiService.login(loginRequest = loginRequest)
                val body = response.body()
                if (response.isSuccessful) {
                    emit(ResultState.Success(data = body))
                } else {
                    val type = object : TypeToken<WrappedResponse<User>>(){}.type
                    val err : WrappedResponse<User> = Gson().fromJson(response.errorBody()!!.charStream(), type)
                    err.code = response.code()
                    emit(ResultState.Error(message = response.message()))
                }
            } catch (e: IOException) {
                emit(ResultState.Error(message = e.message ?: "An error occurred"))
            } catch (e: HttpException) {
                emit(ResultState.Error(message = e.message ?: "An error occurred"))
            }
        }.flowOn(dispatcher)
    }

    override suspend fun loginDemo(): Flow<ResultState<WrappedResponse<User>>> {
        return flow {
            try {
                val response = apiService.loginDemo()
                val body = response.body()
                if (response.isSuccessful) {
                    emit(ResultState.Success(data = body))
                } else {
                    val type = object : TypeToken<WrappedResponse<User>>(){}.type
                    val err : WrappedResponse<User> = Gson().fromJson(response.errorBody()!!.charStream(), type)
                    err.code = response.code()
                    emit(ResultState.Error(message = response.message()))
                }
            } catch (e: IOException) {
                emit(ResultState.Error(message = e.message ?: "An error occurred"))
            } catch (e: HttpException) {
                emit(ResultState.Error(message = e.message ?: "An error occurred"))
            }
        }.flowOn(dispatcher)
    }
}