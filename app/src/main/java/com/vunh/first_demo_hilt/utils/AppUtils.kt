package com.vunh.first_demo_hilt.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import com.vunh.first_demo_hilt.ui.account.AccountActivity
import com.vunh.first_demo_hilt.ui.account.ChangePasswordActivity
import com.vunh.first_demo_hilt.ui.account.UpdateInfoActivity
import com.vunh.first_demo_hilt.ui.home.HomeActivity
import com.vunh.first_demo_hilt.ui.limit.LimitActivity
import com.vunh.first_demo_hilt.ui.login.LoginActivity
import com.vunh.first_demo_hilt.ui.maintenance.MaintenanceActivity
import com.vunh.first_demo_hilt.ui.maintenance.step.StepMaintenanceActivity
import com.vunh.first_demo_hilt.ui.news.NewsActivity
import com.vunh.first_demo_hilt.ui.notification.NotificationActivity
import com.vunh.first_demo_hilt.ui.notification.SettingNotificationActivity
import com.vunh.first_demo_hilt.ui.setting.SettingActivity
import com.vunh.first_demo_hilt.ui.stickheader.SimpleStickHeaderActivity
import com.vunh.first_demo_hilt.ui.support.SupportActivity
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.text.DecimalFormat

object AppUtils {
    fun setAnimationClickItem(view: View, duration: Long = 100) {
        val animation: Animation = AlphaAnimation(0.3f, 1.0f)
        animation.duration = duration
        view.startAnimation(animation)
    }

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun getWidthScreen(marginHorizontal: Int = 0): Int {
        return Resources.getSystem().displayMetrics.widthPixels - marginHorizontal
    }

    fun getHeightScreen(marginVertical: Int = 0) =
        Resources.getSystem().displayMetrics.widthPixels - marginVertical

    fun Context.hideKeyboard(view: View?) {
        val inputMethodManager =
            this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }

    fun getJsonFromAssets(context: Context, fileName: String?): String? {
        val jsonString: String = try {
            val `is`: InputStream = context.assets.open(fileName!!)
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, Charset.forName("UTF-8"))
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return jsonString
    }

    fun doubleToStringDecimalFormat(double: Double?): String {
        return try {
            DecimalFormat("#,##0.00").format(double).toString()
        } catch (ex: Exception) {
            "0"
        }
    }

    fun goToLogin(context: Context) {
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }

    fun goToHome(context: Context) {
        val intent = Intent(context, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }

    fun goToAccount(context: Context) {
        val intent = Intent(context, AccountActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun goToChangePassword(context: Context) {
        val intent = Intent(context, ChangePasswordActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun goToUpdateInfo(context: Context) {
        val intent = Intent(context, UpdateInfoActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun goToNotification(context: Context) {
        val intent = Intent(context, NotificationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun goToSettingNotification(context: Context) {
        val intent = Intent(context, SettingNotificationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun goToSetting(context: Context) {
        val intent = Intent(context, SettingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun goToMaintenance(context: Context) {
        val intent = Intent(context, MaintenanceActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun goToLimit(context: Context) {
        val intent = Intent(context, LimitActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun goToStepMaintenance(context: Context) {
        val intent = Intent(context, StepMaintenanceActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun goToNews(context: Context) {
        val intent = Intent(context, NewsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun goToSupport(context: Context) {
        val intent = Intent(context, SupportActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun goToSimpleStickHeader(context: Context) {
        val intent = Intent(context, SimpleStickHeaderActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}