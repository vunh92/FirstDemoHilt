package com.vunh.first_demo_hilt.ui.maintenance.step

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.base.BaseFragment
import com.vunh.first_demo_hilt.databinding.FragmentSecondStepMaintenanceBinding
import com.vunh.first_demo_hilt.ui.customview.BottomSheetSelectRadio
import com.vunh.first_demo_hilt.ui.customview.EditTextDialog
import com.vunh.first_demo_hilt.utils.extension.showToast

class SecondStepMaintenanceFragment : BaseFragment() {
    private lateinit var binding: FragmentSecondStepMaintenanceBinding
    private val viewModel: StepMaintenanceViewModel by viewModels({requireActivity()})

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSecondStepMaintenanceBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initBindingEvent()
    }

    private fun initBindingEvent() {
        binding.secondMaintenancePeriodTv.setOnClickListener {
            val dialog = BottomSheetSelectRadio(
                selectedPosition = viewModel.selectedPeriodMaintenance,
                items = viewModel.periodMaintenanceData,
            ){ num ->
                if (num >= viewModel.periodMaintenanceData.size - 1) {
                    EditTextDialog(
                        title = getString(R.string.second_maintenance_input_period),
                        maxLength = 2,
                        inputType = InputType.TYPE_CLASS_NUMBER
                    ) { text ->
                        when (text.toInt()) {
                            0 -> {
                                viewModel.selectedPeriodMaintenance = 0
                                viewModel.periodMaintenanceValue.value = 1
                                binding.secondMaintenancePeriodTv.text = getString(R.string.second_maintenance_period_about, "1")
                            }
                            else -> {
                                viewModel.selectedPeriodMaintenance = num
                                viewModel.periodMaintenanceValue.value = text.toInt()
                                binding.secondMaintenancePeriodTv.text = getString(R.string.second_maintenance_period_about, text)
                            }
                        }
                    }.show(requireActivity().supportFragmentManager, EditTextDialog.TAG.toString())
                }else {
                    viewModel.selectedPeriodMaintenance = num
                    viewModel.periodMaintenanceValue.value = num + 1
                    binding.secondMaintenancePeriodTv.text = viewModel.periodMaintenanceData[num]
                }
            }
            dialog.show(requireActivity().supportFragmentManager, BottomSheetSelectRadio.TAG)
        }
        binding.secondMaintenanceDistanceTv.setOnClickListener {
            val dialog = BottomSheetSelectRadio(
                selectedPosition = viewModel.selectedDistanceMaintenance,
                items = viewModel.distanceMaintenanceData,
            ){ num ->
                if (num >= viewModel.distanceMaintenanceData.size - 1) {
                    EditTextDialog(
                        title = getString(R.string.second_maintenance_input_distance),
                        maxLength = 5,
                        inputType = InputType.TYPE_CLASS_NUMBER
                    ) { text ->
                        when (text.toInt()) {
                            0 -> {
                                viewModel.selectedDistanceMaintenance = 0
                                binding.secondMaintenanceDistanceTv.text = getString(R.string.second_maintenance_distance_about, "1000")
                                viewModel.distanceMaintenanceValue.value = 1000
                            }
                            else -> {
                                viewModel.selectedDistanceMaintenance = num
                                binding.secondMaintenanceDistanceTv.text = getString(R.string.second_maintenance_distance_about, text)
                                viewModel.distanceMaintenanceValue.value = text.toInt()
                            }
                        }
                    }.show(requireActivity().supportFragmentManager, EditTextDialog.TAG.toString())
                }else {
                    viewModel.selectedDistanceMaintenance = num
                    viewModel.distanceMaintenanceValue.value = (num + 1)*1000
                    binding.secondMaintenanceDistanceTv.text = viewModel.distanceMaintenanceData[num]
                }
            }
            dialog.show(requireActivity().supportFragmentManager, BottomSheetSelectRadio.TAG)
        }
        binding.secondMaintenancePeriodCkb.setOnCheckedChangeListener { _, isChecked ->
            if (!binding.secondMaintenanceDistanceCkb.isChecked && !isChecked) {
                requireActivity().showToast(getString(R.string.second_maintenance_required_select_one))
                binding.secondMaintenancePeriodCkb.isChecked = true
                return@setOnCheckedChangeListener
            }
            viewModel.isPeriodMaintenanceCkb = isChecked
        }
        binding.secondMaintenanceDistanceCkb.setOnCheckedChangeListener { _, isChecked ->
            if (!binding.secondMaintenancePeriodCkb.isChecked && !isChecked) {
                requireActivity().showToast(getString(R.string.second_maintenance_required_select_one))
                binding.secondMaintenanceDistanceCkb.isChecked = true
                return@setOnCheckedChangeListener
            }
            viewModel.isDistanceMaintenanceCkb = isChecked
        }
        binding.secondMaintenanceNextBtn.setOnClickListener {
            (requireActivity() as StepMaintenanceActivity).nextPage(page = 2)
        }
    }

    private fun initView() {
        binding.secondMaintenancePeriodTv.text = getString(R.string.second_maintenance_period_about, viewModel.periodMaintenanceValue.value.toString())
        binding.secondMaintenanceDistanceTv.text = getString(R.string.second_maintenance_distance_about, viewModel.distanceMaintenanceValue.value.toString())
        binding.secondMaintenancePeriodCkb.isChecked = viewModel.isPeriodMaintenanceCkb
        binding.secondMaintenanceDistanceCkb.isChecked = viewModel.isDistanceMaintenanceCkb
    }

    private fun initViewModel() {
        viewModel.periodMaintenanceValue.observe(requireActivity()) {
            viewModel.calcRemainingDate()
        }
        viewModel.distanceMaintenanceValue.observe(requireActivity()) {
            viewModel.calcRemainingKm()
        }
    }

}