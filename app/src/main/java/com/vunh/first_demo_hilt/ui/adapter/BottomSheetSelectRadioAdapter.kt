package com.vunh.first_demo_hilt.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.radiobutton.MaterialRadioButton
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.utils.AppUtils

class BottomSheetSelectRadioAdapter (
    var selectedPosition: Int = 0,
    listener: Listener,
) : RecyclerView.Adapter<BottomSheetSelectRadioAdapter.ViewHolder>() {

    private var items: MutableList<String> = arrayListOf()
    private var mListener = listener
    lateinit var binding: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetSelectRadioAdapter.ViewHolder {
        binding = LayoutInflater.from(parent.context).inflate(R.layout.item_bottom_sheet_select_radio, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged", "ResourceAsColor")
    override fun onBindViewHolder(holder: BottomSheetSelectRadioAdapter.ViewHolder, position: Int) {
        holder.nameTv.text = items[position]
        if (selectedPosition == holder.bindingAdapterPosition) {
            holder.checkedRadio.isChecked = true
            holder.nameTv.setTextColor(ContextCompat.getColor(binding.context, R.color.colorPrimary))
        }else {
            holder.checkedRadio.isChecked = false
            holder.nameTv.setTextColor(R.color.textColorDefault)
        }
        holder.itemView.setOnClickListener {
            AppUtils.setAnimationClickItem(it)
            notifyItemChanged(selectedPosition)
            selectedPosition = holder.bindingAdapterPosition
            notifyItemChanged(selectedPosition)
            mListener.onItemRadioClick(selectedPosition)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTv: TextView = itemView.findViewById(R.id.name_tv)
        val checkedRadio: MaterialRadioButton = itemView.findViewById(R.id.checked_radio)
    }

    fun getItem(position: Int): String {
        return items[position]
    }

    fun clear() {
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    fun remove(r: String) {
        val position: Int = items.indexOf(r)
        if (position > -1) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }


    fun add(r: String) {
        items.add(r)
        notifyItemInserted(items.size - 1)
    }

    fun addAll(moveResults: List<String>) {
        for (result in moveResults) {
            add(result)
        }
    }

    interface Listener {
        fun onItemRadioClick(position: Int)
    }
}