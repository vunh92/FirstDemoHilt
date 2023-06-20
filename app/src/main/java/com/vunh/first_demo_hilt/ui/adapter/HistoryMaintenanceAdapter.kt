package com.vunh.first_demo_hilt.ui.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vunh.first_demo_hilt.R
import java.util.*

class HistoryMaintenanceAdapter : ListAdapter<String, HistoryMaintenanceAdapter.ViewHolder>(Callback()) {

    lateinit var binding: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = LayoutInflater.from(parent.context).inflate(R.layout.item_history_maintenance, parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged", "ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        if (position == itemCount - 1) {
            holder.dotIv.backgroundTintList = ColorStateList.valueOf(binding.context.getColor(R.color.orange))
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dotIv: ImageView = itemView.findViewById(R.id.item_history_maintenance_dot)
        private val textTv: TextView = itemView.findViewById(R.id.item_history_maintenance_text)

        fun bind(item: String) = with(itemView) {
            textTv.text = item
        }
    }

    override fun submitList(list: MutableList<String>?) {
        super.submitList(ArrayList(list ?: arrayListOf()))
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