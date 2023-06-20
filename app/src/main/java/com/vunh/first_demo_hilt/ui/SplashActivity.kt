package com.vunh.first_demo_hilt.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.vunh.first_demo_hilt.BuildConfig
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.database.AppSharePref
import com.vunh.first_demo_hilt.databinding.ActivitySplashBinding
import com.vunh.first_demo_hilt.utils.AppUtils
import com.vunh.first_demo_hilt.utils.ConstantsTimeDelay
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.AppSettingsDialogHolderActivity
import pub.devrel.easypermissions.EasyPermissions
import java.util.*
import javax.inject.Inject
import kotlin.system.exitProcess


@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity: AppCompatActivity() , EasyPermissions.PermissionCallbacks {
    private lateinit var binding: ActivitySplashBinding
    @Inject
    lateinit var sharedPrefs: AppSharePref
    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
    )

    companion object {
        private const val REQUEST_PERMISSION_CODE = 100
        private const val CALL_PHONE_CODE = 101
        private const val WRITE_EXTERNAL_CODE = 102
        private const val READ_EXTERNAL_CODE = 103
    }

    override fun onResume() {
        super.onResume()
        /* Loop request permission */
//        requestPermission()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.versionNameTv.text = getString(R.string.app_name) + "-" + BuildConfig.VERSION_NAME
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        runSplash()
    }

    private fun runSplash() {
        Thread {
            startProgressBar()
            initData()
            requestPermission()
        }.start()
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_CODE)
    private fun requestPermission() {
        if (EasyPermissions.hasPermissions(this@SplashActivity, *permissions)) {
            direction()
        } else {
            EasyPermissions.requestPermissions(
                this@SplashActivity, getString(R.string.common_required_permission),
                REQUEST_PERMISSION_CODE, *permissions
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this@SplashActivity)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.hasPermissions(this@SplashActivity, *permissions)) {
            direction()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (!EasyPermissions.hasPermissions(this@SplashActivity, *permissions)) {
//            AppSettingsDialog.Builder(this).build().show()
            openSettingsDialog()
        }
//        if (EasyPermissions.somePermissionPermanentlyDenied(this@SplashActivity, perms)) {
//            AppSettingsDialog.Builder(this).build().show()
//        }
    }

    private fun openSettingsDialog() {
        val dialog = AlertDialog.Builder(this@SplashActivity)
        dialog.setTitle(getString(R.string.common_notification))
        dialog.setMessage(getString(R.string.common_required_permission))
        dialog.setOnDismissListener {  }
        dialog.setPositiveButton(R.string.common_ok) { _, _ ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(Uri.fromParts("package", packageName, null))
            startActivityForResult(intent, AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE)
        }
        dialog.setNegativeButton(R.string.common_cancel) { _, _ ->
            finish()
            exitProcess(0)
        }
        dialog.show()
    }

    private fun initData() {
//        getLanguage()
//        AppSharePresfs(this).newInstance().setBoolean(KEY_SCANNING, true)
    }

    private fun getLanguage() {
//        val locale = AppSharePresfs(this).newInstance().getString(KEY_CURRENT_LANGUAGE)
//        if (locale.isNullOrEmpty()) {
//            AppSharePresfs(this).newInstance().setString(KEY_CURRENT_LANGUAGE, LANGUAGE.ENGLISH)
//            Locale.setDefault(Locale(LANGUAGE.ENGLISH))
//        } else {
//            Locale.setDefault(Locale(locale))
//        }
//        resources?.updateConfiguration(Configuration(), DisplayMetrics())
    }

    private fun startProgressBar() {
        var progress = 0
        while (progress < 100) {
            try {
                Thread.sleep(ConstantsTimeDelay.TIME_SPLASH.toLong())
                findViewById<ProgressBar>(R.id.progress_bar).progress = progress
            } catch (e: Exception) {
                e.printStackTrace()
            }
            progress += 1
        }
    }

    private fun direction() {
//        when (sharedPrefs.getToken().isEmpty()) {
//            true -> {
//                AppUtils.gotoLogin(context = this)
//            }
//            else -> {
//                AppUtils.gotoMain(this)
//            }
//        }
        when (!sharedPrefs.getString(AppSharePref.PREF_USER_PROFILE).isNullOrEmpty()) {
            true -> {
                AppUtils.goToHome(this)
            }
            else -> {
                AppUtils.goToLogin(context = this)
            }
        }
        finish()
    }

    override fun onBackPressed() {
//        finish()
//        exitProcess(0)
        return
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            Log.i("resultCode",resultCode.toString())
            if (!EasyPermissions.hasPermissions(this@SplashActivity, *permissions)) {
                finish()
                exitProcess(0)
            }else {
                direction()
            }
        }
    }
}