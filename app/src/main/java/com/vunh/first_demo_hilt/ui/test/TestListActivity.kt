package com.vunh.first_demo_hilt.ui.test

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.base.BaseActivity
import com.vunh.first_demo_hilt.databinding.ActivityTestListBinding
import com.vunh.first_demo_hilt.databinding.ItemTestListBinding
import com.vunh.first_demo_hilt.models.Maintenance
import com.vunh.first_demo_hilt.models.Prediction
import com.vunh.first_demo_hilt.models.TestModel
import com.vunh.first_demo_hilt.ui.adapter.base.*
import com.vunh.first_demo_hilt.ui.customview.ArrayStringDialog
import com.vunh.first_demo_hilt.ui.customview.BottomSheetSelectItem
import com.vunh.first_demo_hilt.ui.maintenance.step.StepMaintenanceActivity
import com.vunh.first_demo_hilt.ui.support.SupportViewModel
import com.vunh.first_demo_hilt.utils.ConstantsIntentKey
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class TestListActivity : BaseActivity() {
    private lateinit var binding: ActivityTestListBinding
    private val viewModel: SupportViewModel by viewModels()
    private lateinit var generalAdapter: GeneralRecyclerViewAdapter<TestModel, ItemTestListBinding>
    var testList: MutableList<TestModel> = arrayListOf()
    var selectedItems: MutableList<TestModel> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        setupToolbar()
        initView()
        initBindingEvent()
    }

    private fun initData() {
        testList.add(TestModel(1, "test 1", "content 1"))
        testList.add(TestModel(2, "test 2", "content 2"))
        testList.add(TestModel(3, "test 3", "content 3"))
        testList.add(TestModel(4, "test 4", "content 4"))
        testList.add(TestModel(5, "test 5", "content 5"))
        testList.add(TestModel(6, "test 6", "content 6"))
        testList.add(TestModel(7, "test 7", "content 7"))
        testList.add(TestModel(8, "test 8", "content 8"))
        testList.add(TestModel(9, "test 9", "content 9"))
        testList.add(TestModel(10, "test 10", "content 10"))
    }

    private fun initBindingEvent() {
    }

    private fun initView() {
        generalAdapter = GeneralRecyclerViewAdapter(R.layout.item_test_list, createBindingInterface())
        binding.testRv.adapter = generalAdapter
        val selectionTracker = SelectionTracker.Builder(
            "type",
            binding.testRv,
            GeneralItemKeyProvider(binding.testRv),
            GeneralItemLookUp(binding.testRv),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(SelectionPredicates.createSelectAnything()).build()

        selectionTracker.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
            override fun onSelectionChanged() {
                super.onSelectionChanged()
//                menuView.findItem(R.id.item_check).isVisible = !selectionTracker.selection.isEmpty
                if (selectionTracker.selection.size() > 0) {
                    selectedItems = selectionTracker.selection.map {
                        generalAdapter.currentList[it.toInt()]
                    }.toMutableList()
                    selectedItems.forEach {
                        Log.i("items", "$it")
                    }
                }
            }

        })

        binding.testRv.apply {
            layoutManager = LinearLayoutManager(this@TestListActivity)
            generalAdapter.selectionTracker = selectionTracker
            generalAdapter.submitList(testList)
        }

    }

    private fun createBindingInterface() =
        object : GeneralRecyclerBindingInterface<ItemTestListBinding, TestModel> {
            override fun bindData(
                binder: ItemTestListBinding,
                model: TestModel,
                clickListener: GeneralClickListener<TestModel>?,
                position: Int
            ) {
                binder.itemTestType.text = model.type.toString()
                binder.itemTestTitle.text = model.title
                binder.itemTestContent.text = model.content
                clickListener?.onClick(model)
            }
        }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
//        binding.toolbar.overflowIcon = getDrawable(R.drawable.ic_more_horiz)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
//        supportActionBar?.title = getString(R.string.account_title)
        supportActionBar?.setDisplayShowTitleEnabled(false)
//        binding.toolbarTitle.text = getString(R.string.account_title)
    }

}