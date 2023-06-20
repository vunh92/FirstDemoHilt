package com.vunh.first_demo_hilt.ui.account

import com.vunh.first_demo_hilt.models.User
import com.vunh.first_demo_hilt.utils.WrappedResponse

sealed class ChangePasswordState  {
    object Init : ChangePasswordState()
    data class Success(val result: Boolean) : ChangePasswordState()
    data class Error(val message: String) : ChangePasswordState()
}