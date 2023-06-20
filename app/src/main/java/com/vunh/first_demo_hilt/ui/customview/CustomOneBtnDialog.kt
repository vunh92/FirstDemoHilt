package com.vunh.first_demo_hilt.ui.customview

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.databinding.DialogCustomOneBtnBinding

class CustomOneBtnDialog(
    private val iconTitle: Int,
    private val title: String,
    private val desc: String,
    private val listener: (Boolean) -> Unit
) : DialogFragment() {
    private lateinit var binding: DialogCustomOneBtnBinding
    companion object {
        val TAG = CustomOneBtnDialog::class.java.name.toString()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCancelable(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCustomOneBtnBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            binding.titleIconIv.setImageResource(iconTitle)
        }catch (ex: Exception) {
            binding.titleIconIv.setImageResource(R.drawable.ic_info)
        }
        binding.titleTv.text = title
        binding.descriptionTv.text = desc
        binding.okBtn.setOnClickListener {
            dismiss()
            listener.invoke(true)
        }
    }
}