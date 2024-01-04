package com.vunh.first_demo_hilt.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vunh.first_demo_hilt.databinding.LayoutHomeBottomSheetSettingBinding

class HomeBottomSheetSetting : BottomSheetDialogFragment() {
    companion object {
        val TAG = HomeBottomSheetSetting::class.java.name.toString()
    }

    private lateinit var binding: LayoutHomeBottomSheetSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutHomeBottomSheetSettingBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.homeBottomSheetSettingLockOn.setOnClickListener {
            Toast.makeText(context, "LockOn", Toast.LENGTH_SHORT).show()
            dismiss()
        }
        binding.homeBottomSheetSettingLockOff.setOnClickListener {
            Toast.makeText(context, "LockOff", Toast.LENGTH_SHORT).show()
            dismiss()
        }
        binding.homeBottomSheetSettingHistory.setOnClickListener {
            Toast.makeText(context, "History", Toast.LENGTH_SHORT).show()
            dismiss()
        }
        binding.homeBottomSheetSettingMaintenance.setOnClickListener {
            Toast.makeText(context, "Maintenance", Toast.LENGTH_SHORT).show()
            dismiss()
        }
        binding.homeBottomSheetSettingStatistical.setOnClickListener {
            Toast.makeText(context, "Statistical", Toast.LENGTH_SHORT).show()
            dismiss()
        }
        binding.homeBottomSheetSettingMore.setOnClickListener {
            Toast.makeText(context, "More", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//    }
}