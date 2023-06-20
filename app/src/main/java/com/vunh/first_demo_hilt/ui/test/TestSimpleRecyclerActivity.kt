package com.vunh.first_demo_hilt.ui.test

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
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
import com.vunh.first_demo_hilt.utils.ConstantsIntentKey
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class TestSimpleRecyclerActivity : BaseActivity() {
    private lateinit var binding: ActivityTestListBinding
    private lateinit var simpleAdapter: SimpleGeneralRecyclerAdapter<TestModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        initView()
        initBindingEvent()
    }

    private fun initBindingEvent() {
    }

    private fun initView() {
//        adapter = GeneralRecyclerViewAdapter(R.layout.item_test_list, createBindingInterface())
//        binding.materialList.adapter = adapter
//        selectionTracker = SelectionTracker.Builder(
//            "material",
//            binding.materialList,
//            GeneralItemKeyProvider(binding.materialList),
//            GeneralItemLookUp(binding.materialList),
//            StorageStrategy.createLongStorage()
//        ).withSelectionPredicate(SelectionPredicates.createSelectAnything()).build()
//
//        selectionTracker.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
//            override fun onSelectionChanged() {
//                super.onSelectionChanged()
//                menuView.findItem(R.id.item_check).isVisible = !selectionTracker.selection.isEmpty
//                if (selectionTracker.selection.size() > 0) {
//                    selectedItems = selectionTracker.selection.map {
//                        adapter.currentList[it.toInt()]
//                    }
//                    selectedItems.forEach {
//                        Log.i("items", "$it")
//                    }
//                }
//            }
//
//        })
//        adapter.selectionTracker = selectionTracker
//        materialViewModel.materialList.observe(viewLifecycleOwner, Observer {
//            adapter.submitList(it)
//        })

    }

    private fun createBindingInterface() =
        object : GeneralSimpleRecyclerBindingInterface<TestModel> {
            override fun bindData(item: TestModel, view: View) {
//                binder.itemTestType.text = model.type.toString()
//                binder.itemTestTitle.text = model.title
//                binder.itemTestContent.text = model.content
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

    private fun showBottomSheetSelection() {
        if (supportFragmentManager.findFragmentByTag(BottomSheetSelectItem.TAG) != null) return
        val dialog = BottomSheetSelectItem(
            isShowClose = true,
            isMultiple = true,
            title = getString(R.string.common_filter),
//            predictionSelected = Prediction("demo", "Demo", true),
            items = Prediction.getListDemo()
        ) { _, _, _, list ->
//            viewModel.maintenanceUserDemo = list
        }
        dialog.show(supportFragmentManager, BottomSheetSelectItem.TAG)
    }

}