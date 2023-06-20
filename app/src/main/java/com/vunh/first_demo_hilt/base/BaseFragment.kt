package com.vunh.first_demo_hilt.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseFragment : Fragment() {
    private val viewModel: BaseViewModel by viewModels()
}