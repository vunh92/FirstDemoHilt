package com.vunh.first_demo_hilt.ui.login

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.base.BaseActivity
import com.vunh.first_demo_hilt.database.AppSharePref
import com.vunh.first_demo_hilt.databinding.ActivityLoginBinding
import com.vunh.first_demo_hilt.models.Profile
import com.vunh.first_demo_hilt.models.Setting
import com.vunh.first_demo_hilt.models.User
import com.vunh.first_demo_hilt.models.dto.LoginRequest
import com.vunh.first_demo_hilt.models.dto.ProfileResponse
import com.vunh.first_demo_hilt.utils.AppUtils
import com.vunh.first_demo_hilt.utils.AppUtils.hideKeyboard
import com.vunh.first_demo_hilt.utils.extension.isEmail
import com.vunh.first_demo_hilt.utils.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private var loginRequest: LoginRequest? = null

    @Inject
    lateinit var sharedPrefs: AppSharePref

    private val openRegisterActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
//            goToHome()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
        initViewModel()
    }

    private fun initData() {
        val userAccountJson = sharedPrefs.getString(AppSharePref.PREF_REMEMBER_ACCOUNT)
        userAccountJson?.let {
            loginRequest = Gson().fromJson(it, LoginRequest::class.java)
            binding.emailEditText.setText(loginRequest?.email)
            binding.passwordEditText.setText(loginRequest?.password)
        }
    }

    private fun initView() {
        binding.loginRemember.isChecked = loginRequest != null
        binding.loginBtn.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            hideKeyboard(binding.root)
            if(validateLogin(email, password)){
                loginRequest = LoginRequest(email = email, password = password)
                viewModel.login(loginRequest!!)
            }
        }
        binding.loginForgotPasswordBtn.setOnClickListener {
            showNotificationPopup(message = getString(R.string.login_show_password)) {}
        }
        binding.loginDemoBt.setOnClickListener {
            sharedPrefs.put(AppSharePref.PREF_IS_DEMO, true)
            loginDemo()
        }
        binding.loginSignUpBt.setOnClickListener {
            showNotDesignPopup {}
        }
        binding.emailEditText.setText("vunh@gmail.com")
        binding.passwordEditText.setText("12345678")
    }

    private fun initViewModel(){
        viewModel.loginState
            .flowWithLifecycle(lifecycle,  Lifecycle.State.STARTED)
            .onEach { state -> handleStateChange(state) }
            .launchIn(lifecycleScope)
        viewModel.showSuccess.observe(this) {
            it?.let {
                showToast(message = it)
            }
        }
    }

    private fun validateLogin(email: String, password: String) : Boolean{
        var result = true
        resetAllInputError()
        if(!email.isEmail()){
            setEmailError(getString(R.string.msg_err_email_not_valid))
            result = false
        }

        if(password.length < 6){
            setPasswordError(getString(R.string.msg_err_password_not_valid))
            result = false
        }
        return result
    }

    private fun resetAllInputError(){
        setEmailError(null)
        setPasswordError(null)
    }

    private fun setEmailError(e : String?){
        binding.emailInput.error = e
    }

    private fun setPasswordError(e: String?){
        binding.passwordInput.error = e
    }

    private fun handleStateChange(state: LoginActivityState){
        when(state){
            is LoginActivityState.Init -> Unit
            is LoginActivityState.ErrorLogin -> handleErrorLogin(state.message)
            is LoginActivityState.SuccessLogin -> handleSuccessLogin(state.user)
            is LoginActivityState.ShowToast -> showToast(state.message)
            is LoginActivityState.IsLoading -> handleLoading(state.isLoading)
        }
    }

    private fun handleErrorLogin(message: String) {
        showNotificationPopup(message = message) {}
    }

    private fun handleLoading(isLoading: Boolean){
        binding.loginBtn.isEnabled = !isLoading
        binding.loginSignUpBt.isEnabled = !isLoading
        binding.loginForgotPasswordBtn.isEnabled = !isLoading
        binding.loginDemoBt.isEnabled = !isLoading
        binding.loginRemember.isEnabled = !isLoading
        if(isLoading) {
            showLoading()
        }else {
            hideLoading()
        }
//        binding.loginLoadingProgressBar.isInvisible = !isLoading
//        binding.loginLoadingProgressBar.isIndeterminate = isLoading
//        if(!isLoading) {
//            binding.loginLoadingProgressBar.progress = 0
//        }
    }

    private fun handleSuccessLogin(user: User?) {
        lifecycleScope.launch {
            if (binding.loginRemember.isChecked) {
                val userAccount = Gson().toJson(loginRequest)
                sharedPrefs.setString(AppSharePref.PREF_REMEMBER_ACCOUNT, userAccount)
            } else {
                sharedPrefs.clearKey(AppSharePref.PREF_REMEMBER_ACCOUNT)
            }
//        sharedPrefs.saveToken(loginEntity.token)
            if (sharedPrefs.get(AppSharePref.PREF_IS_DEMO, Boolean::class.java)) {
                val profileDemo = Gson().fromJson(
                    AppUtils.getJsonFromAssets(
                        this@LoginActivity,
                        "user_demo.json"
                    ), ProfileResponse::class.java
                )
                viewModel.saveProfile(profileDemo.mapToProfile())
                val settingDemo = Gson().fromJson(
                    AppUtils.getJsonFromAssets(
                        this@LoginActivity,
                        "setting_demo.json"
                    ), Setting::class.java
                )
                viewModel.saveSetting(settingDemo)
                goToHome(Gson().toJson(profileDemo))
            } else {
                goToHome(Gson().toJson(user?.mapToProfile()))
            }
        }
    }

    private fun goToRegisterActivity(){
//        binding.registerButton.setOnClickListener {
//            openRegisterActivity.launch(Intent(this@LoginActivity, RegisterActivity::class.java))
//        }
    }

    private fun goToHome(userJson: String){
        sharedPrefs.setString(AppSharePref.PREF_USER_PROFILE, userJson)
        AppUtils.goToHome(this@LoginActivity)
        finish()
    }

    private fun loginDemo(){
//        val userDemoJson = AppUtils.getJsonFromAssets(this@LoginActivity, "user_demo.json")
//        val userDemo = Gson().fromJson(userDemoJson, User::class.java)
//        sharedPrefs.saveToken(userDemoJson ?: "")
        viewModel.loginDemo()
    }
}