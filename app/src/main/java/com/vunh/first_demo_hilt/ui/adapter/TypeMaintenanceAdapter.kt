package com.vunh.first_demo_hilt.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.utils.AppUtils


class TypeMaintenanceAdapter (
    var selectedPosition: Int = 0,
    private val listener: (String) -> Unit
) : ListAdapter<String, TypeMaintenanceAdapter.ViewHolder>(Callback()) {

    lateinit var binding: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = LayoutInflater.from(parent.context).inflate(R.layout.item_type_maintenance, parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged", "ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (selectedPosition == holder.bindingAdapterPosition) {
            holder.typeCard.backgroundTintList = ContextCompat.getColorStateList(binding.context, R.color.greenDark)
        }
        else {
            holder.typeCard.backgroundTintList = ContextCompat.getColorStateList(binding.context, R.color.white)
        }
        holder.itemView.setOnClickListener {
            AppUtils.setAnimationClickItem(it)
            notifyItemChanged(selectedPosition)
            selectedPosition = holder.bindingAdapterPosition
            notifyItemChanged(selectedPosition)
            listener.invoke(getItem(position))
        }
        holder.bind(getItem(position))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val typeCard: CardView = itemView.findViewById(R.id.item_type_card)
        val typeTv: TextView = itemView.findViewById(R.id.item_type_tv)

        fun bind(item: String) = with(itemView) {
            typeTv.text = item
        }
    }

    class Callback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }
}