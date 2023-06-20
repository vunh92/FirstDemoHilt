package com.vunh.first_demo_hilt.ui.maintenance.step

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.base.BaseFragment
import com.vunh.first_demo_hilt.databinding.FragmentFirstStepMaintenanceBinding
import com.vunh.first_demo_hilt.ui.adapter.TypeMaintenanceAdapter
import com.vunh.first_demo_hilt.ui.customview.EditTextDialog
import com.vunh.first_demo_hilt.utils.extension.showToast

class FirstStepMaintenanceFragment : BaseFragment() {
    private lateinit var binding: FragmentFirstStepMaintenanceBinding
    private val viewModel: StepMaintenanceViewModel by viewModels({requireActivity()})
    private var typeMaintenanceAdapter: TypeMaintenanceAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstStepMaintenanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
        initBindingEvent()
    }

    private fun initBindingEvent() {
        binding.firstMaintenanceAddBtn.setOnClickListener {
            EditTextDialog(title = getString(R.string.first_maintenance_add_label)) { text ->
                if (viewModel.typeMaintenanceData.find { it == text } != null) {
                    requireActivity().showToast(getString(R.string.first_maintenance_item_exist, text))
                    return@EditTextDialog
                }
                viewModel.typeMaintenanceData.add(text)
                viewModel.typeMaintenanceValue.value = text
                typeMaintenanceAdapter?.let { adapter ->
                    adapter.submitList(viewModel.typeMaintenanceData)
                    adapter.notifyItemChanged(adapter.selectedPosition)
                    adapter.selectedPosition = viewModel.typeMaintenanceData.size - 1
                    adapter.notifyItemChanged(adapter.selectedPosition)
                    binding.firstMaintenanceTypeRv.smoothScrollToPosition(adapter.selectedPosition)
                }
            }.show(requireActivity().supportFragmentManager, EditTextDialog.TAG.toString())
        }
        binding.firstMaintenanceNextBtn.setOnClickListener {
            (requireActivity() as StepMaintenanceActivity).nextPage(page = 1)
        }
    }

    private fun initView() {
        val selected = viewModel.typeMaintenanceData.indexOf(viewModel.typeMaintenanceValue.value ?: viewModel.typeMaintenanceData.first())
        typeMaintenanceAdapter = TypeMaintenanceAdapter(selected) {
            viewModel.typeMaintenanceValue.value = it
        }
        binding.firstMaintenanceTypeRv.apply {
            layoutManager = LinearLayoutManager(context)
            typeMaintenanceAdapter?.submitList(viewModel.typeMaintenanceData)
            adapter = typeMaintenanceAdapter
        }
    }

    private fun initViewModel() {
//        viewModel.typeMaintenanceValue.observe(requireActivity()) {
//            viewModel.maintenance?.typeMaintenance = it
//        }
    }
}