package com.vunh.first_demo_hilt.ui.stickheader

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.base.BaseActivity
import com.vunh.first_demo_hilt.databinding.ActivityNewsBinding
import com.vunh.first_demo_hilt.databinding.ActivityStickHeaderBinding
import com.vunh.first_demo_hilt.ui.adapter.SimpleStickHeaderAdapter
import com.vunh.first_demo_hilt.ui.adapter.StickHeaderItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SimpleStickHeaderActivity : BaseActivity() {
    private lateinit var binding: ActivityStickHeaderBinding
//    private lateinit var maintenanceListAdapter: MaintenanceListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStickHeaderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        initView()
        initBindingEvent()
    }

    private fun initBindingEvent() {
    }

    private fun initView() {
        val simpleStickHeaderAdapter = SimpleStickHeaderAdapter()

        binding.stickHeaderRv.apply {
            adapter = simpleStickHeaderAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(StickHeaderItemDecoration(simpleStickHeaderAdapter))
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

}