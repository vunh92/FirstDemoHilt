package com.vunh.first_demo_hilt.repositories

import com.vunh.first_demo_hilt.models.User
import com.vunh.first_demo_hilt.utils.WrappedResponse
import com.vunh.first_demo_hilt.utils.states.DataState
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUser(userId: Int): Flow<DataState<WrappedResponse<User>>>
}