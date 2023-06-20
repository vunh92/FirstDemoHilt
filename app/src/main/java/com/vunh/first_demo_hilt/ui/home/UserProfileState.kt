package com.vunh.first_demo_hilt.ui.home

import com.vunh.first_demo_hilt.models.User

sealed class UserProfileState {
    object Init : UserProfileState()
    data class IsLoading(val isLoading: Boolean) : UserProfileState()
    data class Success(val user: User?) : UserProfileState()
    data class Error(val message: String) : UserProfileState()
}
