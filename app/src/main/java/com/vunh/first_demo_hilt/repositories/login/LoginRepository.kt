package com.vunh.first_demo_hilt.repositories.login

import com.vunh.first_demo_hilt.models.User
import com.vunh.first_demo_hilt.models.dto.LoginRequest
import com.vunh.first_demo_hilt.utils.WrappedResponse
import com.vunh.first_demo_hilt.utils.states.ResultState
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun login(loginRequest: LoginRequest) : Flow<ResultState<WrappedResponse<User>>>
    suspend fun loginDemo() : Flow<ResultState<WrappedResponse<User>>>
}