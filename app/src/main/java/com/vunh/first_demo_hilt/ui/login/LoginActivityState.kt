package com.vunh.first_demo_hilt.ui.login

import com.vunh.first_demo_hilt.models.User
import com.vunh.first_demo_hilt.utils.WrappedResponse

sealed class LoginActivityState  {
    object Init : LoginActivityState()
    data class IsLoading(val isLoading: Boolean) : LoginActivityState()
    data class ShowToast(val message: String) : LoginActivityState()
    data class SuccessLogin(val user: User?) : LoginActivityState()
    data class ErrorLogin(val message: String) : LoginActivityState()
}