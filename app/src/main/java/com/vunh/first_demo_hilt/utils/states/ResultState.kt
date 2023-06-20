package com.vunh.first_demo_hilt.utils.states

import com.vunh.first_demo_hilt.ui.home.UserProfileState

sealed class ResultState<T> {
    data class Success<T>(val data: T?) : ResultState<T>()
    data class Error<T>(val message: String = "") : ResultState<T>()
}
