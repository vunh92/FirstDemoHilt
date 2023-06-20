package com.vunh.first_demo_hilt.ui.setting

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.vunh.first_demo_hilt.BuildConfig
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.base.BaseActivity
import com.vunh.first_demo_hilt.databinding.ActivitySettingBinding
import com.vunh.first_demo_hilt.models.Prediction
import com.vunh.first_demo_hilt.ui.customview.BottomSheetSelectItem
import com.vunh.first_demo_hilt.utils.AppUtils
import com.vunh.first_demo_hilt.utils.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingActivity : BaseActivity() {
    private lateinit var binding: ActivitySettingBinding
    private val viewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        initData()
        initView()
        initViewModel()
        initBindingEvent()
    }

    private fun initData() {
//        TODO("Not yet implemented")
    }

    private fun initBindingEvent() {
        binding.settingNotificationLine.setOnClickListener {
            AppUtils.goToSettingNotification(this@SettingActivity)
        }
        binding.settingLanguageLine.setOnClickListener {
            showBottomSheetSelection(
                isMultiple = false,
                title = binding.settingLanguageTitleTv.text.toString(),
                selected = viewModel.languageSelected.value,
                list = viewModel.languageList
            ) { _, _, prediction, _ ->
                prediction?.let {
                    viewModel.languageSelected.value = it
                    showToast(getString(R.string.msg_err_func_not_design))
                    viewModel.setting.value?.apply {
                        this.language = it.id
                    }
                    lifecycleScope.launch {
                        viewModel.updateSetting()
                    }
                }
            }
        }
        binding.settingFormatDateLine.setOnClickListener {
            showBottomSheetSelection(
                isMultiple = false,
                title = binding.settingFormatDateTitleTv.text.toString(),
                selected = viewModel.formatDateSelected.value,
                list = viewModel.formatDateList
            ) { _, _, prediction, _ ->
                prediction?.let {
                    viewModel.formatDateSelected.value = it
                    viewModel.setting.value?.apply {
                        this.formatDate = it.id
                    }
                    lifecycleScope.launch {
                        viewModel.updateSetting()
                    }
                }
            }
        }
        binding.settingTimeRefreshDataLine.setOnClickListener {
            showBottomSheetSelection(
                isMultiple = false,
                title = binding.settingTimeRefreshDataTitleTv.text.toString(),
                selected = viewModel.timeRefreshDataSelected.value,
                list = viewModel.timeRefreshDataList
            ) { _, _, prediction, _ ->
                prediction?.let {
                    viewModel.timeRefreshDataSelected.value = it
                    viewModel.setting.value?.apply {
                        this.timeRefreshData = it.id.toInt()
                    }
                    lifecycleScope.launch {
                        viewModel.updateSetting()
                    }
                }
            }
        }
    }

    private fun initView() {
//        setupToolbar()
        binding.settingAppVersionDescTv.text = BuildConfig.VERSION_NAME
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
        viewModel.showToast.observe(this) {
            it?.let {
                showToast(message = it)
            }
        }
        viewModel.languageSelected.observe(this) { prediction ->
            prediction?.let {
                binding.settingLanguageDescTv.text = it.name
            }
        }
        viewModel.formatDateSelected.observe(this) { prediction ->
            prediction?.let {
                binding.settingFormatDateDescTv.text = it.name
            }
        }
        viewModel.timeRefreshDataSelected.observe(this) { prediction ->
            prediction?.let {
                binding.settingTimeRefreshDataDescTv.text = it.name
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

    private fun showBottomSheetSelection(isMultiple: Boolean, title: String, selected: Prediction?, list: MutableList<Prediction>, listener: (String?, Int?, Prediction?, MutableList<Prediction>) -> Unit) {
        if (supportFragmentManager.findFragmentByTag(BottomSheetSelectItem.TAG) != null) return
        val dialog = BottomSheetSelectItem(
            isShowClose = true,
            isMultiple = isMultiple,
            title = title,
            predictionSelected = selected,
            items = list,
            clickListener = listener
        )
        dialog.show(supportFragmentManager, BottomSheetSelectItem.TAG)
    }
}