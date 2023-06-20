package com.vunh.first_demo_hilt.ui.news

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.vunh.first_demo_hilt.base.BaseActivity
import com.vunh.first_demo_hilt.databinding.ActivityNewsBinding
import com.vunh.first_demo_hilt.ui.adapter.NewsAdapter
import com.vunh.first_demo_hilt.ui.adapter.StickHeaderItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsActivity : BaseActivity() {
    private lateinit var binding: ActivityNewsBinding
    private val viewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        initView()
        initViewModel()
        initBindingEvent()
        lifecycleScope.launch {
            viewModel.getNewsList(this@NewsActivity)
        }
    }

    private fun initBindingEvent() {
    }

    private fun initView() {
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initViewModel() {
        viewModel.isLoading.observe(this) {
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }
        }
        viewModel.showError.observe(this) {
            it?.let {
                showCustomOneBtnPopup(message = it) {}
            }
        }
        viewModel.newsStickyHeaderList.observe(this) {
            if (it.isNullOrEmpty()) {
                binding.layoutNewsEmpty.root.isVisible = true
                binding.layoutNewsRl.isVisible = false
            }else {
                binding.layoutNewsEmpty.root.isVisible = false
                binding.layoutNewsRl.isVisible = true
                val newsAdapter = NewsAdapter(it)
                binding.newsRv.apply {
                    adapter = newsAdapter
                    layoutManager = LinearLayoutManager(context)
                    addItemDecoration(StickHeaderItemDecoration(newsAdapter))
                }
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

}