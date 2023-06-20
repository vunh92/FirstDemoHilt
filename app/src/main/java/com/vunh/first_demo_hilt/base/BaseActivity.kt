package com.vunh.first_demo_hilt.base

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.ui.customview.CustomOneBtnDialog
import com.vunh.first_demo_hilt.ui.customview.LoadingDialog
import com.vunh.first_demo_hilt.ui.customview.NotificationDialog
import com.vunh.first_demo_hilt.utils.AppUtils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {
    private var tag: String = BaseActivity::class.java.simpleName
    var statusBarHeight = 0
    private var loadingDialog: LoadingDialog? = null

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            hideKeyboard(currentFocus ?: View(this))
            currentFocus?.clearFocus()
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        getStatusBarHeight()
    }

    @SuppressLint("DiscouragedApi", "InternalInsetResource")
    private fun getStatusBarHeight() {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun startBrowser(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    fun showLoading() {
        loadingDialog = LoadingDialog().newInstance()
        loadingDialog?.show(supportFragmentManager, LoadingDialog.TAG)
    }

    fun hideLoading() {
        loadingDialog?.dismiss()
    }

    fun showNotDesignPopup(listener: (Boolean) -> Unit) {
        showNotificationPopup(message = getString(R.string.msg_err_func_not_design), listener = listener)
    }

    fun showNotificationPopup(twoButton: Boolean = false, message: String, listener: (Boolean) -> Unit) {
        NotificationDialog(
            twoButton = twoButton,
            desc = message,
            listener = listener
        ).show(supportFragmentManager, tag)
    }

    fun showCustomOneBtnPopup(iconTitle: Int = R.drawable.ic_info, title: String = getString(R.string.common_notification), message: String, listener: (Boolean) -> Unit) {
        CustomOneBtnDialog(
            iconTitle = iconTitle,
            title = title,
            desc = message,
            listener = listener
        ).show(supportFragmentManager, tag)
    }

    fun showDatePickerDialog(year: Int, month: Int, day: Int, maxDate: Long?, minDate: Long?, listener: (Date) -> Unit) {
        val dpd = DatePickerDialog(this, R.style.ThemeDatePickerDialog,{ view, year, monthOfYear, dayOfMonth ->
            val time = Calendar.getInstance()
            time.set(year, monthOfYear, dayOfMonth)
            listener.invoke(time.time)
        }, year, month, day)
        maxDate?.let {
            dpd.datePicker.maxDate = it
        }
        minDate?.let {
            dpd.datePicker.minDate = it
        }
        dpd.show()
    }

}
