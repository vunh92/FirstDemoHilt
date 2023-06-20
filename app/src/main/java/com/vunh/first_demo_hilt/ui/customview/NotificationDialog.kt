package com.vunh.first_demo_hilt.ui.customview

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.databinding.DialogNotificationBinding

class NotificationDialog(
    private val twoButton: Boolean = false,
    private val desc: String,
    private val listener: (Boolean) -> Unit
) : DialogFragment() {
    private lateinit var binding: DialogNotificationBinding
    companion object {
        val TAG = NotificationDialog::class.java.name.toString()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCancelable(twoButton)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.titleTv.text = getString(R.string.common_notification)
        binding.descriptionTv.text = desc
        if (!twoButton) {
            binding.noBtn.text = getString(R.string.common_close)
            binding.yesBtn.isVisible = false
        }
        binding.noBtn.setOnClickListener {
            dismiss()
            listener.invoke(!twoButton)
        }
        binding.yesBtn.setOnClickListener {
            dismiss()
            listener.invoke(true)
        }
    }
}