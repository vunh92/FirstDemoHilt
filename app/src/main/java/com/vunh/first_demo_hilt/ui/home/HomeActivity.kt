package com.vunh.first_demo_hilt.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.*
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.base.BaseActivity
import com.vunh.first_demo_hilt.database.AppDatabase
import com.vunh.first_demo_hilt.databinding.ActivityHomeBinding
import com.vunh.first_demo_hilt.database.AppSharePref
import com.vunh.first_demo_hilt.models.Prediction
import com.vunh.first_demo_hilt.models.Profile
import com.vunh.first_demo_hilt.models.User
import com.vunh.first_demo_hilt.ui.customview.BottomSheetSelectItem
import com.vunh.first_demo_hilt.ui.notification.NotificationActivity
import com.vunh.first_demo_hilt.ui.test.TestListActivity
import com.vunh.first_demo_hilt.utils.AppUtils
import com.vunh.first_demo_hilt.utils.DATABASE_NAME
import com.vunh.first_demo_hilt.utils.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : BaseActivity() , NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var drawerToggle: ActionBarDrawerToggle
//    private var profile: Profile? = null
    @Inject
    lateinit var sharedPrefs: AppSharePref
    @Inject
    lateinit var appDatabase: AppDatabase

    override fun onResume() {
        super.onResume()
        viewModel.getProfile()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
        initViewModel()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(drawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        when (item.itemId) {
            R.id.home_menu_notification -> {
                AppUtils.goToNotification(this@HomeActivity)
            }
            R.id.home_menu_car -> {
                binding.drawerLayout.openDrawer(GravityCompat.END)
            }
            R.id.home_menu_test -> {
                val intent = Intent(this@HomeActivity, TestListActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.home_item_drawer_account -> {
                AppUtils.goToAccount(this@HomeActivity)
            }
            R.id.home_item_drawer_maintenance -> {
                AppUtils.goToMaintenance(this@HomeActivity)
            }
            R.id.home_item_drawer_limit -> {
                AppUtils.goToLimit(this@HomeActivity)
            }
            R.id.home_item_drawer_news -> {
                AppUtils.goToNews(this@HomeActivity)
            }
            R.id.home_item_drawer_support -> {
//                AppUtils.goToSupport(this@HomeActivity)
//                AppUtils.goToSimpleStickHeader(this@HomeActivity)
            }
            R.id.home_item_drawer_setting -> {
                AppUtils.goToSetting(this@HomeActivity)
            }
            R.id.home_item_drawer_logout -> {
                logout()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initData() {
//        profile = Gson().fromJson(sharedPrefs.getString(AppSharePref.PREF_USER_PROFILE), Profile::class.java)
    }

    private fun initView() {
        setupDrawer()
        setupBottomSheet()
//        setupProfile(profile)
    }

    private fun initViewModel() {
//        viewModel.userProfileState
//            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
//            .onEach { state -> handleUserProfileStateChange(state) }
//            .launchIn(lifecycleScope)
        viewModel.showError.observe(this@HomeActivity) {
            it?.let {
                showCustomOneBtnPopup(
                    message = it
                ) {}
            }
        }
        viewModel.showSuccess.observe(this) {
            it?.let {
                showToast(message = it)
            }
        }
        viewModel.profile.observe(this) {
            it?.let {
                setupProfile(it)
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun setupDrawer() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = getString(R.string.app_name)
        drawerToggle = ActionBarDrawerToggle(
            this@HomeActivity,
            binding.drawerLayout,
            R.string.nav_open,
            R.string.nav_close
        )
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        binding.navView.setNavigationItemSelectedListener(this@HomeActivity)
        binding.navViewSecond.setNavigationItemSelectedListener(this@HomeActivity)
        binding.navView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            topMargin = statusBarHeight
        }
        binding.navViewSecond.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            topMargin = statusBarHeight
        }

        /* margin status bar height */
//        ViewCompat.setOnApplyWindowInsetsListener(binding.navViewSecond) { view, windowInsets ->
//            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars())
//            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
//                topMargin = insets.top
//            }
//            WindowInsetsCompat.CONSUMED
//        }
        /* get Id drawer header */
//        val headerDrawer = binding.navView.getHeaderView(0)
//        headerDrawer.findViewById<TextView>(R.id.tv_header).setOnClickListener {
//            showToast("tap text header")
//        }
//        headerDrawer.findViewById<Button>(R.id.btn_header).setOnClickListener {
//            showToast("tap btn header")
//        }
    }

    private fun setupBottomSheet() {
        val homeBottomSheetBehavior = BottomSheetBehavior.from(binding.layoutBottomSheet.bottomSheet)
        homeBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        homeBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        homeBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        homeBottomSheetBehavior.isHideable = false
        val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // Do something for new state.
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Do something for slide offset.
            }
        }
        // To add the callback:
        homeBottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)
        // To remove the callback:
        homeBottomSheetBehavior.removeBottomSheetCallback(bottomSheetCallback)

        val settingBtn = binding.layoutBottomSheet.bottomSheet.findViewById<MaterialButton>(R.id.bottom_sheet_setting_btn)
        settingBtn.setOnClickListener {
            bottomSheetSetting()
        }
    }

    private fun bottomSheetSetting() {
        if (supportFragmentManager.findFragmentByTag(HomeBottomSheetSetting.TAG) != null) return
        val dialog = HomeBottomSheetSetting()
        dialog.show(supportFragmentManager, HomeBottomSheetSetting.TAG)
    }

    @SuppressLint("SetTextI18n")
    private fun setupProfile(profile: Profile?) {
        val headerDrawer = binding.navView.getHeaderView(0)
        val headerAvatar = headerDrawer.findViewById<CircleImageView>(R.id.header_avatar_im)
        val headerTitle = headerDrawer.findViewById<TextView>(R.id.header_title_tv)
        val headerDetail = headerDrawer.findViewById<TextView>(R.id.header_detail_tv)
        Glide.with(this@HomeActivity)
            .load(profile?.avatar)
            .placeholder(R.drawable.avatar)
            .error(R.drawable.avatar)
            .into(headerAvatar)
        headerTitle.text = profile?.firstName
        headerDetail.text = profile?.email
    }

    private fun handleUserProfileStateChange(state: UserProfileState) {
        when(state){
            is UserProfileState.Init -> Unit
            is UserProfileState.Error -> showNotificationPopup(message = state.message){}
            is UserProfileState.Success -> handleSuccessUserProfile(state.user)
            is UserProfileState.IsLoading -> handleLoading(state.isLoading)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleSuccessUserProfile(user: User?) {
//        val userProfile = Gson().toJson(user)
//        user?.let {
//            sharedPrefs.setString(AppSharePref.PREF_USER_PROFILE, userProfile)
//            setupProfile(it)
//        }
    }

    private fun handleLoading(isLoading: Boolean) {

    }

    private fun logout() {
        lifecycleScope.launch {
            viewModel.isLoading.value = true
            sharedPrefs.clearToken()
            sharedPrefs.clearKey(AppSharePref.PREF_USER_PROFILE)
            viewModel.profile.value?.let {
                viewModel.deleteProfile(it.id ?: 0)
            }
            viewModel.clearAllDatabase()
            viewModel.isLoading.value = false
            AppUtils.goToLogin(this@HomeActivity)
            finish()
        }
    }
}