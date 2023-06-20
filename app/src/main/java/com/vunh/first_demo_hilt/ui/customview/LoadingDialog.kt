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

class LoadingDialog: DialogFragment() {
    companion object {
        val TAG = LoadingDialog::class.java.name.toString()
        private var loadingDialog: LoadingDialog? = null
    }

    fun newInstance(): LoadingDialog {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog()
        }
        return loadingDialog as LoadingDialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCancelable(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.layout_loading_dialog, container, false)
    }

}