package com.vunh.first_demo_hilt.ui.maintenance.history

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.base.BaseActivity
import com.vunh.first_demo_hilt.database.AppSharePref
import com.vunh.first_demo_hilt.databinding.ActivityHistoryMaintenanceBinding
import com.vunh.first_demo_hilt.models.Maintenance
import com.vunh.first_demo_hilt.models.Profile
import com.vunh.first_demo_hilt.ui.adapter.HistoryMaintenanceAdapter
import com.vunh.first_demo_hilt.utils.ConstantsIntentKey
import com.vunh.first_demo_hilt.utils.DAY_PER_PERIOD
import com.vunh.first_demo_hilt.utils.DISTANCE_PER_DAY
import com.vunh.first_demo_hilt.utils.extension.calcDayBetweenDate
import com.vunh.first_demo_hilt.utils.extension.getDateLater
import com.vunh.first_demo_hilt.utils.extension.toStringDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class HistoryMaintenanceActivity : BaseActivity() {
    private lateinit var binding: ActivityHistoryMaintenanceBinding
    private val viewModel: HistoryMaintenanceViewModel by viewModels()
    private lateinit var historyMaintenanceAdapter: HistoryMaintenanceAdapter
    @Inject
    lateinit var sharedPrefs: AppSharePref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryMaintenanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        setupToolbar()
        initView()
        initViewModel()
    }

    private fun initData() {
        viewModel.profile.value = Gson().fromJson(sharedPrefs.getString(AppSharePref.PREF_USER_PROFILE), Profile::class.java)
        intent.getStringExtra(ConstantsIntentKey.MAINTENANCE_DATA)?.let {
            viewModel.maintenance = Gson().fromJson(it, Maintenance::class.java)
        }
    }

    private fun initView() {
        viewModel.profile.observe(this) {
            it?.let {
                viewModel.getHistoryMaintenanceList()
            }
        }
        viewModel.maintenance?.let {
            initMaintenance(it)
        }
        historyMaintenanceAdapter = HistoryMaintenanceAdapter()
        binding.historyMaintenanceListRv.apply {
            adapter = historyMaintenanceAdapter
            layoutManager = LinearLayoutManager(this@HistoryMaintenanceActivity)
        }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun initViewModel() {
        viewModel.isLoading.observe(this) {
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }
        }
        viewModel.showError.observe(this) {
            it?.let {
                showCustomOneBtnPopup(message = it) {}
            }
        }
        viewModel.itemList.observe(this) {
            it?.let { historyMaintenanceAdapter.submitList(it) }
        }
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
//        binding.toolbarTitle.text = getString(R.string.history_maintenance_title)
    }

    private fun initMaintenance(item: Maintenance) {
        lifecycleScope.launch {
            val days = calcDayBetweenDate(
                startDate = item.lastTimeMaintenance ?: viewModel.currentTime,
                endDate = viewModel.currentTime,
            )
            binding.itemTypeTv.text = "${item.typeMaintenance}"
            binding.itemNextMaintenanceDateTv.text = getDateLater(viewModel.maintenance?.lastTimeMaintenance, item.periodMaintenance!!* DAY_PER_PERIOD).toStringDate()
            binding.itemRemainingDateTv.text = getString(R.string.last_maintenance_remaining_date_maintenance, "${item.periodMaintenance!!* DAY_PER_PERIOD - days}")
            if (item.isDistanceMaintenance) {
                binding.itemDistanceTv.text = getString(R.string.maintenance_item_distance, "${days* DISTANCE_PER_DAY} / ${item.distanceMaintenance}")
                binding.itemDistanceTv.isVisible = true
            }else {
                binding.itemDistanceTv.isVisible = false
            }
            if (item.isPeriodMaintenance) {
                binding.itemPeriodTv.text = getString(R.string.maintenance_item_period, "$days / ${item.periodMaintenance!!* DAY_PER_PERIOD}")
                binding.itemPeriodTv.isVisible = true
            }else {
                binding.itemPeriodTv.isVisible = false
            }
            binding.itemLastTimeTv.text = item.lastTimeMaintenance.toStringDate()
            if (item.noteMaintenance.isNullOrEmpty()) {
                binding.itemNoteLine.isVisible = false
            }else {
                binding.itemNoteTv.text = item.noteMaintenance
                binding.itemNoteLine.isVisible = true
            }
            val progress = 100 * (item.periodMaintenance!!* DAY_PER_PERIOD - days)/(item.periodMaintenance!!* DAY_PER_PERIOD)
            if (progress <= 20) {
                binding.itemExpectedTv.setTextColor(ColorStateList.valueOf(getColor(R.color.red)))
                binding.itemNextMaintenanceDateTv.setTextColor(ColorStateList.valueOf(getColor(R.color.red)))
                binding.itemRemainingDateTv.setTextColor(ColorStateList.valueOf(getColor(R.color.red)))
            }else if (progress <= 50) {
                binding.itemExpectedTv.setTextColor(ColorStateList.valueOf(getColor(R.color.orange)))
                binding.itemNextMaintenanceDateTv.setTextColor(ColorStateList.valueOf(getColor(R.color.orange)))
                binding.itemRemainingDateTv.setTextColor(ColorStateList.valueOf(getColor(R.color.orange)))
            }
        }
    }

}