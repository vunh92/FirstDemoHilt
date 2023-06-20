package com.vunh.first_demo_hilt.repositories.profile

import com.vunh.first_demo_hilt.models.Profile
import com.vunh.first_demo_hilt.utils.states.ResultState

interface ProfileRepository {
    suspend fun insert(profile: Profile) : ResultState<Profile>
    suspend fun delete(id: Int) : ResultState<Int>
    suspend fun update(profile: Profile) : ResultState<Profile>
    suspend fun clear() : ResultState<Boolean>
    suspend fun getProfile() : ResultState<Profile>
}