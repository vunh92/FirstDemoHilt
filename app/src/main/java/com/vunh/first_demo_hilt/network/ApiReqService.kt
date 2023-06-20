package com.vunh.first_demo_hilt.network

import com.vunh.first_demo_hilt.models.User
import com.vunh.first_demo_hilt.models.dto.LoginRequest
import com.vunh.first_demo_hilt.models.dto.LoginResponse
import com.vunh.first_demo_hilt.utils.WrappedResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiReqService {

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest
    ) : Response<WrappedResponse<User>>

    @GET("api/users/1")
    suspend fun loginDemo() : Response<WrappedResponse<User>>

    @GET("api/users/{id}")
    suspend fun getUserAsync(
        @Path("id") userId: Int,
    ): Response<WrappedResponse<User>>

//    @GET("houses")
//    suspend fun getHouses(
//        @Query("page") page: Int,
//        @Query("pageSize") pageSize: Int
//    ): Response<List<Houses>>

//    @GET("characters/{id}")
//    suspend fun getCharacterDetail(
//        @Path("id") charId: Int
//    ): Response<CharacterDetails>
}