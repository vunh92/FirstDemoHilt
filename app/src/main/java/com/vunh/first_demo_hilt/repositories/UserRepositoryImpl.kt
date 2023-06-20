package com.vunh.first_demo_hilt.repositories

import com.vunh.first_demo_hilt.models.User
import com.vunh.first_demo_hilt.network.ApiReqService
import com.vunh.first_demo_hilt.utils.WrappedResponse
import com.vunh.first_demo_hilt.utils.states.DataState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiReqService,
    private val dispatcher: CoroutineDispatcher
) : UserRepository {
    override suspend fun getUser(userId: Int): Flow<DataState<WrappedResponse<User>>> {
        return flow {
            emit(DataState.loading())
            try {
                val response = apiService.getUserAsync(userId = userId)
                val user = response.body()
                if (response.isSuccessful) {
                    emit(DataState.data(data = user))
                } else {
                    emit(DataState.error(message = response.message()))
                }
            } catch (e: IOException) {
                emit(DataState.error(message = e.message ?: "An error occurred"))
            } catch (e: HttpException) {
                emit(DataState.error(message = e.message ?: "An error occurred"))
            }
        }.flowOn(dispatcher)
//        return flow {
//            emit(DataState.loading())
//            try {
//                    val response = apiService.getUserAsync(userId = userId)
//                    val user = response.body()
//
//                    if (response.isSuccessful) {
//                        emit(DataState.data(data = user))
//                    } else {
//                        emit(DataState.error(message = response.message()))
//                    }
//                } catch (e: IOException) {
//                    emit(
//                        DataState.error(
//                            message = e.message ?: "An error occurred"
//                        )
//                    )
//                } catch (e: HttpException) {
//                    emit(
//                        DataState.error(
//                            message = e.message ?: "An error occurred"
//                        )
//                    )
//            }
//        }.flowOn(dispatcher)
//        return flow {
//            try {
//                val result = withContext(Dispatchers.IO) { apiService.getUserAsync(userId) }
//                ResultState.Success(result)
//            } catch (ex: Throwable) {
//                ResultState.Error(ex.message ?: "")
//            }
//        }
    }
}