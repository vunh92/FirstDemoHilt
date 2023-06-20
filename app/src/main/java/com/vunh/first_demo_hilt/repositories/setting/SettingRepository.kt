package com.vunh.first_demo_hilt.repositories.setting

import com.vunh.first_demo_hilt.models.Setting
import com.vunh.first_demo_hilt.utils.states.ResultState

interface SettingRepository {
    suspend fun insert(setting: Setting) : ResultState<Setting>
    suspend fun delete(id: Int) : ResultState<Int>
    suspend fun update(setting: Setting) : ResultState<Setting>
    suspend fun clear() : ResultState<Boolean>
    suspend fun getSetting(id: Int) : ResultState<Setting>
}