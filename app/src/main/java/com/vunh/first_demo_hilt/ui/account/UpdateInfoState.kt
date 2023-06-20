package com.vunh.first_demo_hilt.ui.account

import com.vunh.first_demo_hilt.models.User
import com.vunh.first_demo_hilt.utils.WrappedResponse

sealed class UpdateInfoState  {
    object Init : UpdateInfoState()
    data class Success(val result: Boolean) : UpdateInfoState()
    data class Error(val message: String) : UpdateInfoState()
}