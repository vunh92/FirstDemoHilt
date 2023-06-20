package com.vunh.first_demo_hilt.ui.customview

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.vunh.first_demo_hilt.R

class CustomToast {
    companion object {
        private var customToast: CustomToast? = null
        private var toast: Toast? = null
    }

    @SuppressLint("InflateParams")
    fun getInstance(context: Context, message: String): CustomToast {
        if (customToast == null) {
            customToast = CustomToast()
        }
        if (toast != null) {
            toast!!.cancel()
        }
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout: View = inflater.inflate(R.layout.layout_custom_toast, null)
        val tv = layout.findViewById<View>(R.id.tv_message) as TextView
        tv.text = message
        toast = Toast(context)
        toast?.duration = Toast.LENGTH_SHORT
        toast?.setGravity(Gravity.BOTTOM, 0, 50)
        toast?.view = layout
        return customToast!!
    }

    fun show() {
        toast?.show()
    }
}