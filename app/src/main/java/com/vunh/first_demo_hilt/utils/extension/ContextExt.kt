package com.vunh.first_demo_hilt.utils.extension

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.ui.customview.CustomToast

fun Context.showToast(message: String) {
    CustomToast().getInstance(this, message).show()
}

fun Context.showGenericAlertDialog(message: String){
    AlertDialog.Builder(this).apply {
        setTitle(getString(R.string.common_notification))
        setMessage(message)
        setPositiveButton(getString(R.string.common_close)){ dialog, _ ->
            dialog.dismiss()
        }
        setCancelable(false)
    }.show()
}

fun Context.showNotDesignMaterialDialog() {
    MaterialAlertDialogBuilder(this)
        .setTitle(getString(R.string.common_notification))
        .setMessage(getString(R.string.msg_err_func_not_design))
        .setPositiveButton(getString(R.string.common_close)) { dialog, _ ->
            dialog.dismiss()
        }
        .setCancelable(false)
        .show()
}

fun Context.showMaterialDialog(title: String, message: String, listener: () -> Unit) {
    MaterialAlertDialogBuilder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(getString(R.string.common_close)) { dialog, _ ->
            listener.invoke()
            dialog.dismiss()
        }
        .setCancelable(false)
        .show()
}

fun Context.showYesNoMaterialDialog(title: String, message: String, listener: (Boolean) -> Unit) {
    MaterialAlertDialogBuilder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(getString(R.string.common_yes)) { dialog, _ ->
            dialog.dismiss()
            listener.invoke(true)
        }
        .setNegativeButton(getString(R.string.common_no)) { dialog, _ ->
            dialog.dismiss()
            listener.invoke(false)
        }
        .setCancelable(false)
        .show()
}