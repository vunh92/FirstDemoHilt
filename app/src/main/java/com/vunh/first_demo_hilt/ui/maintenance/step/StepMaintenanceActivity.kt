package com.vunh.first_demo_hilt.ui.maintenance.step

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.vunh.first_demo_hilt.base.BaseActivity
import com.vunh.first_demo_hilt.database.AppSharePref
import com.vunh.first_demo_hilt.databinding.ActivityStepMaintenanceBinding
import com.vunh.first_demo_hilt.models.Maintenance
import com.vunh.first_demo_hilt.models.Profile
import com.vunh.first_demo_hilt.utils.ConstantsIntentKey
import com.vunh.first_demo_hilt.utils.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StepMaintenanceActivity : BaseActivity() {
    private lateinit var binding: ActivityStepMaintenanceBinding
    private val viewModel: StepMaintenanceViewModel by viewModels()
    @Inject
    lateinit var sharedPrefs: AppSharePref
    private lateinit var viewStateAdapter: ViewStateAdapter
    private val numTab = 4
    private lateinit var firstFragment: FirstStepMaintenanceFragment
    private lateinit var secondFragment: SecondStepMaintenanceFragment
    private lateinit var thirdFragment: ThirdStepMaintenanceFragment
    private lateinit var lastFragment: LastStepMaintenanceFragment

    companion object {
        const val RESULT_STEP_MAINTENANCE_CODE = 1111
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStepMaintenanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        setupToolbar()
        createSliderFragment()
        initViewModel()
        initBindingEvent()
    }

    private fun initBindingEvent() {
//        binding.maintenanceSettingBtn.setOnClickListener {
//            AppUtils.goToSettingNotification(this@MaintenanceActivity)
//            showAddMaintenanceDialog()
//        }
    }

    private fun initData() {
        viewModel.initMaintenance(Gson().fromJson(intent.getStringExtra(ConstantsIntentKey.MAINTENANCE_DATA), Maintenance::class.java))
        intent.getStringExtra(ConstantsIntentKey.PROFILE_DATA)?.let {
            viewModel.profile.value = Gson().fromJson(it, Profile::class.java)
        }
    }

    private fun initViewModel() {
        viewModel.isLoading.observe(this) {
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }
        }
//        viewModel.showSuccess.observe(this) {
//            it?.let {
//                intent.putExtra(ConstantsIntentKey.MAINTENANCE_DATA, Gson().toJson(viewModel.getMaintenance()))
//                setResult(RESULT_STEP_MAINTENANCE_CODE, intent)
//                finish()
//            }
//        }
//        viewModel.showError.observe(this) {
//            it?.let {
//                showToast(message = it)
//            }
//        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
//        binding.toolbar.overflowIcon = getDrawable(R.drawable.ic_more_horiz)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
//        supportActionBar?.title = getString(R.string.account_title)
        supportActionBar?.setDisplayShowTitleEnabled(false)
//        binding.toolbarTitle.text = getString(R.string.account_title)
    }

    private fun createSliderFragment() {
        firstFragment = FirstStepMaintenanceFragment()
        secondFragment = SecondStepMaintenanceFragment()
        thirdFragment = ThirdStepMaintenanceFragment()
        lastFragment = LastStepMaintenanceFragment()
        viewStateAdapter = ViewStateAdapter(this)
        binding.viewPager.adapter = viewStateAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager){tab, positon ->
            tab.view.isClickable = false
            tab.view.isLongClickable = false
        }.attach()
    }

    fun nextPage(page: Int) {
        binding.viewPager.setCurrentItem(page, true)
    }

    private inner class ViewStateAdapter(activity: FragmentActivity) :
        FragmentStateAdapter(activity) {

        override fun getItemCount(): Int = numTab

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> {
                    return firstFragment
                }
                1 -> {
                    return secondFragment
                }
                2 -> {
                    return thirdFragment
                }
                else -> {
                    return lastFragment
                }
            }
        }
    }
}