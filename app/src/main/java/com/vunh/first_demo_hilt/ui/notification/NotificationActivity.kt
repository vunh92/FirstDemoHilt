package com.vunh.first_demo_hilt.ui.notification

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.base.BaseActivity
import com.vunh.first_demo_hilt.databinding.ActivityNotificationBinding
import com.vunh.first_demo_hilt.models.Prediction
import com.vunh.first_demo_hilt.ui.customview.BottomSheetSelectItem
import com.vunh.first_demo_hilt.utils.AppUtils
import com.vunh.first_demo_hilt.utils.extension.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationActivity : BaseActivity() {
    private lateinit var binding: ActivityNotificationBinding
    private val viewModel: NotificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initBindingEvent()
    }

    private fun initBindingEvent() {
        binding.notificationSettingBtn.setOnClickListener {
            AppUtils.goToSettingNotification(this@NotificationActivity)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.notification_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.notification_menu_filter -> {
                showBottomSheetSelection()
            }
            R.id.notification_menu_delete_all -> {
                showToast("notification_menu_delete_all")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView() {
        setupToolbar()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.overflowIcon = getDrawable(R.drawable.ic_more_horiz)
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
            items = viewModel.notificationListDemo
        ) { _, _, _, list ->
//            viewModel.notificationListDemo = list
        }
        dialog.show(supportFragmentManager, BottomSheetSelectItem.TAG)
    }
}