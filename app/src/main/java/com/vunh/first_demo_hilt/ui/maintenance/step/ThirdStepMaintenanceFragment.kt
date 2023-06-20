package com.vunh.first_demo_hilt.ui.maintenance.step

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.base.BaseActivity
import com.vunh.first_demo_hilt.base.BaseFragment
import com.vunh.first_demo_hilt.databinding.FragmentThirdStepMaintenanceBinding
import com.vunh.first_demo_hilt.ui.customview.BottomSheetSelectRadio
import com.vunh.first_demo_hilt.utils.extension.*
import java.util.*

class ThirdStepMaintenanceFragment : BaseFragment() {
    private lateinit var binding: FragmentThirdStepMaintenanceBinding
    private val viewModel: StepMaintenanceViewModel by viewModels({requireActivity()})

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentThirdStepMaintenanceBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initBindingEvent()
    }

    private fun initBindingEvent() {
        binding.thirdMaintenanceSelectMaintenanceCard.setOnClickListener {
            val dialog = BottomSheetSelectRadio(
                selectedPosition = viewModel.selectedLastTimeMaintenance,
                items = viewModel.lastTimeMaintenanceData,
            ){ num ->
                if (num >= viewModel.lastTimeMaintenanceData.size - 1) {
                    (requireActivity() as BaseActivity).showDatePickerDialog(
                        year = viewModel.currentTime.get(Calendar.YEAR),
                        month = viewModel.currentTime.get(Calendar.MONTH),
                        day = viewModel.currentTime.get(Calendar.DAY_OF_MONTH),
                        maxDate = viewModel.currentTime.timeInMillis,
                        minDate = null,
                    ) {
                        viewModel.selectedLastTimeMaintenance = num
                        if (viewModel.currentTime.time.toStringDate() == it.toStringDate()) {
                            binding.thirdMaintenanceSelectMaintenanceTv.text = getString(R.string.third_maintenance_today)
                        }else {
                            binding.thirdMaintenanceSelectMaintenanceTv.text = it.toStringDate()
                        }
                        viewModel.lastTimeMaintenanceValue.value = it
                    }
                }else {
                    viewModel.selectedLastTimeMaintenance = num
                    binding.thirdMaintenanceSelectMaintenanceTv.text = viewModel.lastTimeMaintenanceData[num]
                    viewModel.lastTimeMaintenanceValue.value = getDateLater(day = -30*(num+1))
                }
            }
            dialog.show(requireActivity().supportFragmentManager, BottomSheetSelectRadio.TAG)
        }
        binding.thirdMaintenanceNextBtn.setOnClickListener {
            (requireActivity() as StepMaintenanceActivity).nextPage(page = 3)
        }
    }

    private fun initView() {
//        TODO("Not yet implemented")
    }

    private fun initViewModel() {
        viewModel.lastTimeMaintenanceValue.observe(requireActivity()) {
            viewModel.calcDays()
        }
    }

}