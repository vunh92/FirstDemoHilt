package com.vunh.first_demo_hilt.ui.customview

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.vunh.first_demo_hilt.databinding.DialogArrayStringBinding

class ArrayStringDialog(
    private val title: String,
    private val list: MutableList<String>,
    private val listener: (String) -> Unit
) : DialogFragment() {
    private lateinit var binding: DialogArrayStringBinding
    companion object {
        val TAG = ArrayStringDialog::class.java.name.toString()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog?.setCancelable(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogArrayStringBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.titleTv.text = title
        binding.descriptionLv.apply {
//            layoutManager = LinearLayoutManager(view.context)
            adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, list)
//            serialAdapter.submitList(listAddSerial)
        }
        binding.descriptionLv.setOnItemClickListener { _, _, i, _ ->
            listener.invoke(list[i])
            dismiss()
        }
        binding.closeBtn.setOnClickListener {
            dismiss()
        }
    }
}