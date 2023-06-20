package com.vunh.first_demo_hilt.ui.notification

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.vunh.first_demo_hilt.base.BaseActivity
import com.vunh.first_demo_hilt.databinding.ActivitySettingNotificationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingNotificationActivity : BaseActivity() {
    private lateinit var binding: ActivitySettingNotificationBinding
    private val viewModel: NotificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        initView()
        initViewModel()
        initBindingEvent()
    }

    private fun initBindingEvent() {
        binding.settingNotificationOutInRestrictedAreaSc.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setting.value?.apply {
                outInRestrictedArea = isChecked
            }
        }
        binding.settingNotificationForgotTurnOffCarSc.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setting.value?.apply {
                forgotTurnOffCar = isChecked
            }
        }
        binding.settingNotificationDisconnectedOverAnHourSc.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setting.value?.apply {
                disconnectedOverAnHour = isChecked
            }
        }
        binding.settingNotificationCheckedIv.setOnClickListener {
            lifecycleScope.launch {
                viewModel.updateSettingNotification()
            }
        }
    }

    private fun initView() {
//        setupToolbar()
    }

    private fun initViewModel() {
        viewModel.isLoading.observe(this) {
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }
        }
        viewModel.showSuccess.observe(this) {
            it?.let {
                showCustomOneBtnPopup(message = it) {}
            }
        }
        viewModel.showError.observe(this) {
            it?.let {
                showCustomOneBtnPopup(message = it) {}
            }
        }
        viewModel.setting.observe(this) { setting ->
            setting?.let {
                binding.settingNotificationOutInRestrictedAreaSc.isChecked = it.outInRestrictedArea
                binding.settingNotificationForgotTurnOffCarSc.isChecked = it.forgotTurnOffCar
                binding.settingNotificationDisconnectedOverAnHourSc.isChecked = it.disconnectedOverAnHour
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
//        supportActionBar?.title = getString(R.string.account_title)
        supportActionBar?.setDisplayShowTitleEnabled(false)
//        binding.toolbarTitle.text = getString(R.string.account_title)
    }
}