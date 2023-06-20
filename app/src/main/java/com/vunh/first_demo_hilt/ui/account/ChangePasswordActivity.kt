package com.vunh.first_demo_hilt.ui.account

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.base.BaseActivity
import com.vunh.first_demo_hilt.database.AppSharePref
import com.vunh.first_demo_hilt.databinding.ActivityChangePasswordBinding
import com.vunh.first_demo_hilt.utils.AppUtils.hideKeyboard
import com.vunh.first_demo_hilt.utils.ConstantsIntentKey
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChangePasswordActivity : BaseActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private val viewModel: AccountViewModel by viewModels()
    @Inject
    lateinit var sharedPrefs: AppSharePref

    companion object {
        const val RESULT_CHANGE_PASSWORD_CODE = 1111
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        initData()
        initView()
        initViewModel()
        initBindingEvent()
    }

    private fun initBindingEvent() {
        binding.accountChangePasswordBtn.setOnClickListener {
            hideKeyboard(binding.root)
            lifecycleScope.launch {
                if (sharedPrefs.get(AppSharePref.PREF_IS_DEMO, Boolean::class.java)) {
                    val oldPassword = binding.accountOldPasswordEdt.text.toString().trim()
                    val newPassword = binding.accountNewPasswordEdt.text.toString().trim()
                    val confirmPassword = binding.accountConfirmNewPasswordEdt.text.toString().trim()
                    if (validatePassword(oldPassword, newPassword, confirmPassword)) {
                        viewModel.updatePassword(
                            oldPassword = oldPassword,
                            newPassword = newPassword,
                        )
                    }
                }
            }
        }
    }

    private fun initData() {
//        viewModel.userProfile.value = Gson().fromJson(sharedPrefs.getString(AppSharePref.PREF_USER_PROFILE), Profile::class.java)
    }

    private fun initView() {
        setupToolbar()
    }

    private fun initViewModel() {
        viewModel.isLoading.observe(this) {
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }
        }
        viewModel.updatePasswordState.observe(this) {
            viewModel.isLoading.value = false
            when (it) {
                is ChangePasswordState.Success -> handleSuccess(it.result)
                is ChangePasswordState.Error -> handleError(it.message)
            }
        }
    }

    private fun handleError(message: String) {
        if (message.isNotEmpty()) {
            showCustomOneBtnPopup(message = message) {}
        }
    }

    private fun handleSuccess(result: Boolean) {
        if (result) {
            showCustomOneBtnPopup(message = getString(R.string.common_update_success)) {
                val intent = intent
                intent.putExtra(ConstantsIntentKey.PROFILE_DATA, Gson().toJson(viewModel.profile.value))
                setResult(RESULT_CHANGE_PASSWORD_CODE, intent)
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

    private fun validatePassword(oldPassword: String, newPassword: String, confirmPassword: String) : Boolean{
        var result = true
        resetAllInputError()
        if(oldPassword.length < 6){
            setOldPasswordError(getString(R.string.msg_err_password_not_valid))
            result = false
        }
        if(newPassword.length < 6){
            setNewPasswordError(getString(R.string.msg_err_password_not_valid))
            result = false
        }
        if(confirmPassword.length < 6){
            setConfirmPasswordError(getString(R.string.msg_err_password_not_valid))
            result = false
        }
        if(confirmPassword != newPassword){
            setConfirmPasswordError(getString(R.string.msg_err_confirm_password_not_valid))
            result = false
        }
        return result
    }

    private fun resetAllInputError(){
        setOldPasswordError(null)
        setNewPasswordError(null)
        setConfirmPasswordError(null)
    }

    private fun setOldPasswordError(e: String?){
        binding.accountOldPasswordInput.error = e
    }

    private fun setNewPasswordError(e: String?){
        binding.accountNewPasswordInput.error = e
    }

    private fun setConfirmPasswordError(e: String?){
        binding.accountConfirmNewPasswordInput.error = e
    }
}