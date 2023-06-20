package com.vunh.first_demo_hilt.ui.customview

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vunh.first_demo_hilt.databinding.LayoutBottomSheetSelectRadioBinding
import com.vunh.first_demo_hilt.ui.adapter.BottomSheetSelectRadioAdapter

@SuppressLint("ValidFragment")
class BottomSheetSelectRadio(
    var selectedPosition: Int = 0,
    private var items: MutableList<String>,
    private val clickListener: (Int) -> Unit
) : BottomSheetDialogFragment(), BottomSheetSelectRadioAdapter.Listener {
    companion object {
        val TAG = BottomSheetSelectRadio::class.java.name.toString()
    }
    private lateinit var bottomSheetSelectRadioAdapter: BottomSheetSelectRadioAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = LayoutBottomSheetSelectRadioBinding.inflate(layoutInflater)
        if (selectedPosition >= items.size) selectedPosition = items.size -1
        bottomSheetSelectRadioAdapter = BottomSheetSelectRadioAdapter(selectedPosition = selectedPosition, listener = this)
        binding.itemRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = bottomSheetSelectRadioAdapter
            bottomSheetSelectRadioAdapter.addAll(items)
        }
        return binding.root
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener { dialogInterface: DialogInterface ->
            val dialogc = dialogInterface as BottomSheetDialog
            val bottomSheet: FrameLayout = dialogc.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
            bottomSheetBehavior = BottomSheetBehavior.from<View?>(bottomSheet)
            bottomSheetBehavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels
//            bottomSheetBehavior.peekHeight = resources.getDimensionPixelSize(R.dimen.nav_header_height)
//            bottomSheetBehavior.maxHeight = DisplayMetrics.DENSITY_XHIGH
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return bottomSheetDialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    override fun onItemRadioClick(position: Int) {
        clickListener.invoke(position)
        this.dismiss()
    }

}