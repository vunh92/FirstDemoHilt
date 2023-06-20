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
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vunh.first_demo_hilt.databinding.LayoutBottomSheetSelectItemBinding
import com.vunh.first_demo_hilt.models.Prediction
import com.vunh.first_demo_hilt.ui.adapter.PredictionAdapter

@SuppressLint("ValidFragment")
class BottomSheetSelectItem(
    private var isShowClose: Boolean = false,
    private var isMultiple: Boolean = false,
    private var title: String,
    private var predictionSelected: Prediction? = null,
    private var items: MutableList<Prediction>,
    private val clickListener: (String?, Int?, Prediction?, MutableList<Prediction>) -> Unit
) : BottomSheetDialogFragment(), PredictionAdapter.Listener {
    companion object {
        val TAG = BottomSheetSelectItem::class.java.name.toString()
    }
    private var predictionItem: Prediction? = null
    private var positionItem: Int? = null
    private lateinit var predictionAdapter: PredictionAdapter
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
        val binding = LayoutBottomSheetSelectItemBinding.inflate(layoutInflater)
        binding.titleTv.text = title
        binding.closeTv.isVisible = isShowClose
        binding.closeTv.setOnClickListener {
            dialog?.let { it1 -> onDismiss(it1) }
        }
        predictionAdapter = PredictionAdapter(isMultiple = isMultiple, predictionSelected = predictionSelected, listener = this)
        binding.itemRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = predictionAdapter
            predictionAdapter.addAll(items)
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
        clickListener.invoke(tag, positionItem, predictionItem, items)
    }

    override fun onItemPredictionClick(position: Int, prediction: Prediction?, list: MutableList<Prediction>) {
        if (!isMultiple) {
            predictionItem = prediction
            positionItem = position
            items = list
            this.dismiss()
        } else {
            items = list
        }
    }
}