package com.vunh.first_demo_hilt.ui.customview

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.databinding.DialogEditTextBinding

class EditTextDialog(
    private val title: String,
    private val inputText: String? = null,
    private val maxLength: Int? = null,
    private val inputType: Int? = InputType.TYPE_CLASS_TEXT,
    private val isInputEmpty: Boolean? = false,
    private val listener: (String) -> Unit
) : DialogFragment() {
    private lateinit var binding: DialogEditTextBinding
    companion object {
        val TAG = EditTextDialog::class.java
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogEditTextBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupClickListeners()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun setupView() {
        binding.labelTv.text = title
        maxLength?.let {
            binding.textEdt.filters += InputFilter.LengthFilter(it)
            binding.textInput.counterMaxLength = it
            binding.textInput.isCounterEnabled = true
        }
        inputType?.let {
            binding.textEdt.inputType = it
        }
        inputText?.let {
            binding.textEdt.setText(it)
        }
    }

    private fun setupClickListeners() {
        binding.btnOkDialog.setOnClickListener {
            if(binding.textEdt.text.toString().trim().isBlank() && isInputEmpty == false) {
                return@setOnClickListener
            }
            listener.invoke(binding.textEdt.text.toString())
            dismiss()
        }
        binding.btnCancelDialog.setOnClickListener { dismiss() }
    }
}