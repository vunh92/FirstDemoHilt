package com.vunh.first_demo_hilt.ui.account

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.base.BaseActivity
import com.vunh.first_demo_hilt.database.AppSharePref
import com.vunh.first_demo_hilt.databinding.ActivityAccountBinding
import com.vunh.first_demo_hilt.models.Profile
import com.vunh.first_demo_hilt.utils.AppUtils
import com.vunh.first_demo_hilt.utils.ConstantsIntentKey
import com.vunh.first_demo_hilt.utils.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AccountActivity : BaseActivity() {
    private lateinit var binding: ActivityAccountBinding
    private val viewModel: AccountViewModel by viewModels()
    @Inject
    lateinit var sharedPrefs: AppSharePref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
        initViewModel()
        initBindingEvent()
    }

    private fun initBindingEvent() {
        binding.accountLinkGoogleSc.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateLinkGoogle(isChecked)
        }
        binding.accountLinkFacebookSc.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateLinkFacebook(isChecked)
        }
        binding.accountLinkZaloSc.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateLinkZalo(isChecked)
        }
    }

    private fun initData() {
//        profile = Gson().fromJson(sharedPrefs.getString(AppSharePref.PREF_USER_PROFILE), Profile::class.java)
    }

    private fun initView() {
        setupToolbar()
//        setupInfo()
    }

    private fun initViewModel() {
        viewModel.isLoading.observe(this) {
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }
        }
        viewModel.profile.observe(this) {
            it?.let {
                setupInfo(it)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupInfo(profile: Profile) {
        profile.let {
            Glide.with(this)
                .load(it.avatar)
                .placeholder(R.drawable.avatar)
                .error(R.drawable.avatar)
                .into(binding.accountAvatarIm)
            binding.accountNameTv.text = it.firstName
            binding.accountInfoPhone.text = it.phone
            binding.accountInfoEmail.text = it.email
            binding.accountInfoAddress.text = it.address
            binding.accountInfoTotalCar.text = it.totalCar.toString()
            binding.accountLinkGoogleSc.isChecked = it.linkGoogle
            binding.accountLinkFacebookSc.isChecked = it.linkFacebook
            binding.accountLinkZaloSc.isChecked = it.linkZalo
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.account_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.account_menu_change_password -> {
//                AppUtils.goToChangePassword(this)
                val intent = Intent(this, ChangePasswordActivity::class.java)
                intent.putExtra(ConstantsIntentKey.PROFILE_DATA, Gson().toJson(viewModel.profile.value))
                openChangePasswordActivity.launch(intent)
            }
            R.id.account_menu_update_info -> {
//                AppUtils.goToUpdateInfo(this)
                val intent = Intent(this, UpdateInfoActivity::class.java)
                intent.putExtra(ConstantsIntentKey.PROFILE_DATA, Gson().toJson(viewModel.profile.value))
                openUpdateInfoActivity.launch(intent)
            }
//            R.id.account_menu_logout -> {
//                logout()
//            }
        }
        return super.onOptionsItemSelected(item)
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

//    private fun logout() {
//        lifecycleScope.launch {
//            sharedPrefs.clearToken()
//            sharedPrefs.clearKey(AppSharePref.PREF_USER_PROFILE)
//            viewModel.profile.value?.let {
//                viewModel.clearProfile()
//            }
//            AppUtils.goToLogin(this@AccountActivity)
//        }
//    }

    private val openUpdateInfoActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == UpdateInfoActivity.RESULT_UPDATE_INFO_CODE) {
            val data: Intent? = result.data
            val profileJson = data?.getStringExtra(ConstantsIntentKey.PROFILE_DATA)
            Gson().fromJson(profileJson, Profile::class.java).let {
                viewModel.profile.value = it
            }
        }
    }

    private val openChangePasswordActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == ChangePasswordActivity.RESULT_CHANGE_PASSWORD_CODE) {
            viewModel.getProfile()
        }
    }
}