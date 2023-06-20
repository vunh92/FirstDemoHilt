package com.vunh.first_demo_hilt.ui.account

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.base.BaseActivity
import com.vunh.first_demo_hilt.database.AppSharePref
import com.vunh.first_demo_hilt.databinding.ActivityUpdateInfoBinding
import com.vunh.first_demo_hilt.models.Profile
import com.vunh.first_demo_hilt.utils.AppUtils.hideKeyboard
import com.vunh.first_demo_hilt.utils.ConstantsIntentKey
import com.vunh.first_demo_hilt.utils.extension.isEmail
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UpdateInfoActivity : BaseActivity() {
    private lateinit var binding: ActivityUpdateInfoBinding
    private val viewModel: AccountViewModel by viewModels()
    @Inject
    lateinit var sharedPrefs: AppSharePref

    companion object {
        const val RESULT_UPDATE_INFO_CODE = 2222
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        initData()
        initView()
        initViewModel()
        initBindingEvent()
    }

    private fun initBindingEvent() {
        binding.accountUpdateBtn.setOnClickListener {
            hideKeyboard(binding.root)
            lifecycleScope.launch {
                if (sharedPrefs.get(AppSharePref.PREF_IS_DEMO, Boolean::class.java)) {
                    val usernameEdt = binding.accountUsernameEdt.text.toString().trim()
                    val mailEdt = binding.accountMailEdt.text.toString().trim()
                    val addressEdt = binding.accountAddressEdt.text.toString().trim()
                    if (validateUpdateInfo(usernameEdt, mailEdt, addressEdt)) {
                        viewModel.updateInfo(
                            username = usernameEdt,
                            email = mailEdt,
                            address = addressEdt,
                        )
                    }
                }
            }
        }
    }

    private fun initData() {
//        viewModel.profile = Gson().fromJson(sharedPrefs.getString(AppSharePref.PREF_USER_PROFILE), Profile::class.java)
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
        viewModel.updateInfoState.observe(this) {
            viewModel.isLoading.value = false
            when (it) {
                is UpdateInfoState.Success -> handleSuccess(it.result)
                is UpdateInfoState.Error -> handleError(it.message)
            }
        }
        viewModel.profile.observe(this) {
            it?.let {
                setupInput(it)
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
                setResult(RESULT_UPDATE_INFO_CODE, intent)
                onBackPressedDispatcher.onBackPressed()
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

    private fun setupInput(profile: Profile) {
        binding.accountUsernameEdt.setText(profile.firstName)
        binding.accountMailEdt.setText(profile.email)
        binding.accountAddressEdt.setText(profile.address)
    }

    private fun validateUpdateInfo(username: String, email: String, address: String) : Boolean{
        var result = true
        resetAllInputError()
        if(username.isEmpty()){
            setUsernameError(String.format(getString(R.string.common_field_not_empty), getString(R.string.field_username)))
            result = false
        }
        if(!email.isEmail()){
            setMailError(getString(R.string.msg_err_email_not_valid))
            result = false
        }
        if(address.isEmpty()){
            setAddressError(String.format(getString(R.string.common_field_not_empty), getString(R.string.field_address)))
            result = false
        }
        return result
    }

    private fun resetAllInputError(){
        setUsernameError(null)
        setMailError(null)
        setAddressError(null)
    }

    private fun setUsernameError(e: String?){
        binding.accountUsernameInput.error = e
    }

    private fun setMailError(e: String?){
        binding.accountMailInput.error = e
    }

    private fun setAddressError(e: String?){
        binding.accountAddressInput.error = e
    }

}