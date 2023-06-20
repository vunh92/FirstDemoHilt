package com.vunh.first_demo_hilt.ui.maintenance

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.base.BaseActivity
import com.vunh.first_demo_hilt.databinding.ActivityMaintenanceBinding
import com.vunh.first_demo_hilt.models.Maintenance
import com.vunh.first_demo_hilt.models.Prediction
import com.vunh.first_demo_hilt.ui.adapter.MaintenanceListAdapter
import com.vunh.first_demo_hilt.ui.customview.ArrayStringDialog
import com.vunh.first_demo_hilt.ui.customview.BottomSheetSelectItem
import com.vunh.first_demo_hilt.ui.maintenance.history.HistoryMaintenanceActivity
import com.vunh.first_demo_hilt.ui.maintenance.step.StepMaintenanceActivity
import com.vunh.first_demo_hilt.utils.ConstantsIntentKey
import com.vunh.first_demo_hilt.utils.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class MaintenanceActivity : BaseActivity() {
    private lateinit var binding: ActivityMaintenanceBinding
    private val viewModel: MaintenanceViewModel by viewModels()
    private lateinit var maintenanceListAdapter: MaintenanceListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMaintenanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        initView()
        initViewModel()
        initBindingEvent()
    }

    private fun initBindingEvent() {
        binding.layoutMaintenanceEmpty.maintenanceSettingBtn.setOnClickListener {
            showAddMaintenanceDialog()
        }
        binding.maintenanceListLabelIv.setOnClickListener {
            lifecycleScope.launch {
//                viewModel.clearAllDatabase()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.maintenance_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.maintenance_menu_filter -> {
                showBottomSheetSelection()
            }
            R.id.maintenance_menu_add -> {
                showAddMaintenanceDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView() {
        maintenanceListAdapter = MaintenanceListAdapter(object: MaintenanceListAdapter.Listener {
            override fun onItem(maintenance: Maintenance) {
                val intent = Intent(this@MaintenanceActivity, HistoryMaintenanceActivity::class.java)
                intent.putExtra(ConstantsIntentKey.MAINTENANCE_DATA, Gson().toJson(maintenance))
                intent.putExtra(ConstantsIntentKey.PROFILE_DATA, Gson().toJson(maintenance))
                openStepMaintenanceActivity.launch(intent)
            }

            override fun onMaintenance(maintenance: Maintenance) {
                showDatePickerDialog(
                    year = viewModel.currentTime.get(Calendar.YEAR),
                    month = viewModel.currentTime.get(Calendar.MONTH),
                    day = viewModel.currentTime.get(Calendar.DAY_OF_MONTH),
                    maxDate = viewModel.currentTime.timeInMillis,
                    minDate = maintenance.lastTimeMaintenance?.time,
                ) {
                    val newMaintenance = maintenance.copy(id = maintenance.id, lastTimeMaintenance = it)
                    lifecycleScope.launch {
                        viewModel.updateMaintenance(newMaintenance)
                    }
                }
            }

            override fun onEdit(maintenance: Maintenance) {
                val intent = Intent(this@MaintenanceActivity, StepMaintenanceActivity::class.java)
                intent.putExtra(ConstantsIntentKey.MAINTENANCE_DATA, Gson().toJson(maintenance))
                openStepMaintenanceActivity.launch(intent)
            }

            override fun onDelete(maintenance: Maintenance) {
                showNotificationPopup(
                    twoButton = true,
                    message = getString(R.string.msg_question_you_want_to_delete),
                ) {
                    if (it) {
                        lifecycleScope.launch {
                            viewModel.deleteMaintenance(maintenance)
                        }
                    }
                }
            }
        })
        binding.maintenanceListRv.apply {
            adapter = maintenanceListAdapter
            layoutManager = LinearLayoutManager(this@MaintenanceActivity)
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
        viewModel.maintenanceFilterList.observe(this) {
            if (it.isNotEmpty()) {
                binding.layoutMaintenanceEmpty.root.isVisible = false
                binding.layoutMaintenanceListRl.isVisible = true
                binding.maintenanceListLabelTv.text = "${viewModel.maintenanceUserDemo.first().name} (${it.size})"
                maintenanceListAdapter.submitList(it)
            }else {
                binding.layoutMaintenanceEmpty.root.isVisible = true
                binding.layoutMaintenanceListRl.isVisible = false
            }
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
//        binding.toolbarTitle.text = getString(R.string.account_title)
    }

    private fun showBottomSheetSelection() {
        if (supportFragmentManager.findFragmentByTag(BottomSheetSelectItem.TAG) != null) return
        val dialog = BottomSheetSelectItem(
            isShowClose = true,
            isMultiple = true,
            title = getString(R.string.common_filter),
//            predictionSelected = Prediction("demo", "Demo", true),
            items = Prediction.getListDemo()
        ) { _, _, _, list ->
//            viewModel.maintenanceUserDemo = list
        }
        dialog.show(supportFragmentManager, BottomSheetSelectItem.TAG)
    }

    private fun showAddMaintenanceDialog() {
        ArrayStringDialog(getString(R.string.maintenance_select_car_need_maintenance), viewModel.maintenanceUserDemo.map { it.name }.toMutableList()) {
//            AppUtils.goToStepMaintenance(this@MaintenanceActivity)
            val intent = Intent(this@MaintenanceActivity, StepMaintenanceActivity::class.java)
            intent.putExtra(ConstantsIntentKey.PROFILE_DATA, Gson().toJson(viewModel.profile.value))
            openStepMaintenanceActivity.launch(intent)
        }.show(supportFragmentManager, ArrayStringDialog.TAG)
    }

    private val openStepMaintenanceActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == StepMaintenanceActivity.RESULT_STEP_MAINTENANCE_CODE) {
            val data: Intent? = result.data
            val maintenance = Gson().fromJson(data?.getStringExtra(ConstantsIntentKey.MAINTENANCE_DATA), Maintenance::class.java)
            maintenance?.let {
                lifecycleScope.launch {
                    viewModel.getMaintenanceList()
                }
            }
        }
    }
}