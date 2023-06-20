package com.vunh.first_demo_hilt.ui.maintenance.step

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.base.BaseFragment
import com.vunh.first_demo_hilt.databinding.FragmentLastStepMaintenanceBinding
import com.vunh.first_demo_hilt.ui.customview.EditTextDialog
import com.vunh.first_demo_hilt.utils.ConstantsIntentKey
import com.vunh.first_demo_hilt.utils.extension.*
import kotlinx.coroutines.launch

class LastStepMaintenanceFragment : BaseFragment() {
    private lateinit var binding: FragmentLastStepMaintenanceBinding
    private val viewModel: StepMaintenanceViewModel by viewModels({requireActivity()})

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLastStepMaintenanceBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initBindingEvent()
    }

    private fun initBindingEvent() {
        binding.lastMaintenanceChangeBtn.setOnClickListener {
            EditTextDialog(title = getString(R.string.last_maintenance_content_note), isInputEmpty = true) { text ->
                updateNote(text)
            }.show(requireActivity().supportFragmentManager, EditTextDialog.TAG.toString())
        }
        binding.lastMaintenanceCompleteBtn.setOnClickListener {
            lifecycleScope.launch {
                if (viewModel.maintenance == null) {
                    viewModel.insertMaintenance(viewModel.getCurrentMaintenance())
                }else {
                    viewModel.updateMaintenance(viewModel.getCurrentMaintenance())
                }
            }
        }
    }

    private fun initView() {
        binding.lastMaintenanceNoteCard.isVisible = !viewModel.noteMaintenanceValue.value.isNullOrEmpty()
        binding.lastMaintenanceRemainingKmLine.isVisible = viewModel.isDistanceMaintenanceCkb
        binding.lastMaintenanceNextMaintenanceDateLine.isVisible = viewModel.isPeriodMaintenanceCkb
        binding.lastMaintenanceRemainingDateTv.isVisible = viewModel.isPeriodMaintenanceCkb
    }

    @SuppressLint("ResourceAsColor")
    private fun initViewModel() {
        viewModel.isSuccess.observe(requireActivity()) {
            if (it) {
                val intent = requireActivity().intent
                intent.putExtra(ConstantsIntentKey.MAINTENANCE_DATA, Gson().toJson(viewModel.maintenance))
                requireActivity().setResult(StepMaintenanceActivity.RESULT_STEP_MAINTENANCE_CODE, intent)
                requireActivity().finish()
            }
        }
        viewModel.nextMaintenance.observe(requireActivity()) {
            binding.lastMaintenanceNextMaintenanceDateTv.text = it.toStringDate()
        }
        viewModel.remainingKmMaintenance.observe(requireActivity()) {
            if (it > 0) {
                binding.lastMaintenanceRemainingKmTv.text = getString(R.string.last_maintenance_km, it.toString())
                binding.lastMaintenanceRemainingKmTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.textColorDefault))
            }else {
                binding.lastMaintenanceRemainingKmTv.text = getString(R.string.last_maintenance_due_date)
                binding.lastMaintenanceRemainingKmTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            }
        }
        viewModel.remainingDateMaintenance.observe(requireActivity()) {
            if (it > 0) {
                binding.lastMaintenanceRemainingDateTv.text = getString(R.string.last_maintenance_remaining_date_maintenance, it.toString())
                binding.lastMaintenanceRemainingDateTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.textColorDetail))
            }else {
                binding.lastMaintenanceRemainingDateTv.text = getString(R.string.last_maintenance_due_date)
                binding.lastMaintenanceRemainingDateTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            }
        }
        viewModel.days.observe(requireActivity()) {
            viewModel.calcRemainingDate()
            viewModel.calcRemainingKm()
        }
    }

    private fun updateNote(text: String) {
        viewModel.noteMaintenanceValue.value = text
        binding.lastMaintenanceNoteTv.text = text
        binding.lastMaintenanceNoteCard.isVisible = text.isNotEmpty()
    }
}