package com.vunh.first_demo_hilt.ui.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.google.android.material.button.MaterialButton
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.models.Maintenance
import com.vunh.first_demo_hilt.utils.AppUtils
import com.vunh.first_demo_hilt.utils.DAY_PER_PERIOD
import com.vunh.first_demo_hilt.utils.DISTANCE_PER_DAY
import com.vunh.first_demo_hilt.utils.extension.*
import java.util.*

class MaintenanceListAdapter (
    val listener: Listener
) : ListAdapter<Maintenance, MaintenanceListAdapter.ViewHolder>(Callback()) {

    lateinit var binding: View
    private val currentTime = getToday()

    interface Listener {
        fun onItem(maintenance: Maintenance)
        fun onMaintenance(maintenance: Maintenance)
        fun onEdit(maintenance: Maintenance)
        fun onDelete(maintenance: Maintenance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = LayoutInflater.from(parent.context).inflate(R.layout.item_maintenance_list, parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged", "ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, currentTime)
        holder.itemView.setOnClickListener {
            AppUtils.setAnimationClickItem(it)
            listener.onItem(item)
        }
        holder.expandIv.setOnClickListener {
            TransitionManager.beginDelayedTransition(holder.cardView, ChangeBounds().setDuration(100L))
//            TransitionManager.beginDelayedTransition(holder.cardView, AutoTransition())
            if (holder.groupLine.visibility == 0) {
                holder.groupLine.isVisible = false
                holder.expandIv.setImageResource(R.drawable.ic_arrow_down)
            }else {
                holder.groupLine.isVisible = true
                holder.expandIv.setImageResource(R.drawable.ic_arrow_up)
            }
        }
        holder.maintenanceBtn.setOnClickListener {
            listener.onMaintenance(item)
        }
        holder.editBtn.setOnClickListener {
            listener.onEdit(item)
        }
        holder.deleteBtn.setOnClickListener {
            listener.onDelete(item)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.item_maintenance_list_card)
        val expandIv: ImageView = itemView.findViewById(R.id.item_maintenance_list_expand_iv)
        val groupLine: LinearLayout = itemView.findViewById(R.id.item_maintenance_list_group_line)
        val maintenanceBtn: MaterialButton = itemView.findViewById(R.id.item_maintenance_list_maintenance_btn)
        val editBtn: MaterialButton = itemView.findViewById(R.id.item_maintenance_list_edit_btn)
        val deleteBtn: MaterialButton = itemView.findViewById(R.id.item_maintenance_list_delete_btn)
        private val typeTv: TextView = itemView.findViewById(R.id.item_maintenance_list_type_tv)
        private val expectedTv: TextView = itemView.findViewById(R.id.item_maintenance_list_expected_tv)
        private val nextMaintenanceDateTv: TextView = itemView.findViewById(R.id.item_maintenance_list_next_maintenance_date_tv)
        private val remainingDateTv: TextView = itemView.findViewById(R.id.item_maintenance_list_remaining_date_tv)
        private val distanceTv: TextView = itemView.findViewById(R.id.item_maintenance_list_distance_tv)
        private val periodTv: TextView = itemView.findViewById(R.id.item_maintenance_list_period_tv)
        private val lastTimeTv: TextView = itemView.findViewById(R.id.item_maintenance_list_last_time_tv)
        private val noteLine: LinearLayout = itemView.findViewById(R.id.item_maintenance_list_note_line)
        private val noteTv: TextView = itemView.findViewById(R.id.item_maintenance_list_note_tv)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.item_maintenance_list_progress_bar)

        fun bind(item: Maintenance, currentTime: Date) = with(itemView) {
            val days = calcDayBetweenDate(
                startDate = item.lastTimeMaintenance ?: currentTime,
                endDate = currentTime,
            )
            typeTv.text = "${item.typeMaintenance}"
            nextMaintenanceDateTv.text = getDateLater(item.lastTimeMaintenance, item.periodMaintenance!!*DAY_PER_PERIOD).toStringDate()
            remainingDateTv.text = context.getString(R.string.last_maintenance_remaining_date_maintenance, "${item.periodMaintenance!!*DAY_PER_PERIOD - days}")
            if (item.isDistanceMaintenance) {
                distanceTv.text = context.getString(R.string.maintenance_item_distance, "${days*DISTANCE_PER_DAY} / ${item.distanceMaintenance}")
                distanceTv.isVisible = true
            }else {
                distanceTv.isVisible = false
            }
            if (item.isPeriodMaintenance) {
                periodTv.text = context.getString(R.string.maintenance_item_period, "$days / ${item.periodMaintenance!!*DAY_PER_PERIOD}")
                periodTv.isVisible = true
            }else {
                periodTv.isVisible = false
            }
            lastTimeTv.text = item.lastTimeMaintenance.toStringDate()
            if (item.noteMaintenance.isNullOrEmpty()) {
                noteLine.isVisible = false
            }else {
                noteTv.text = item.noteMaintenance
                noteLine.isVisible = true
            }
            val progress = 100 * (item.periodMaintenance!!*DAY_PER_PERIOD - days)/(item.periodMaintenance!!*DAY_PER_PERIOD)
            progressBar.progress = progress
            if (progress <= 20) {
                progressBar.progressTintList = ColorStateList.valueOf(context.getColor(R.color.red))
                progressBar.progressBackgroundTintList = ColorStateList.valueOf(context.getColor(R.color.red))
                expectedTv.setTextColor(ColorStateList.valueOf(context.getColor(R.color.red)))
                nextMaintenanceDateTv.setTextColor(ColorStateList.valueOf(context.getColor(R.color.red)))
                remainingDateTv.setTextColor(ColorStateList.valueOf(context.getColor(R.color.red)))
            }else if (progress <= 50) {
                progressBar.progressTintList = ColorStateList.valueOf(context.getColor(R.color.orange))
                progressBar.progressBackgroundTintList = ColorStateList.valueOf(context.getColor(R.color.orange))
                expectedTv.setTextColor(ColorStateList.valueOf(context.getColor(R.color.orange)))
                nextMaintenanceDateTv.setTextColor(ColorStateList.valueOf(context.getColor(R.color.orange)))
                remainingDateTv.setTextColor(ColorStateList.valueOf(context.getColor(R.color.orange)))
            }
        }
    }

    override fun submitList(list: MutableList<Maintenance>?) {
        super.submitList(ArrayList(list ?: arrayListOf()))
    }

    class Callback : DiffUtil.ItemCallback<Maintenance>() {
        override fun areItemsTheSame(oldItem: Maintenance, newItem: Maintenance): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Maintenance, newItem: Maintenance): Boolean {
            return oldItem == newItem
        }

//        override fun getChangePayload(oldItem: Maintenance, newItem: Maintenance): Any? {
//            return super.getChangePayload(oldItem, newItem)
//        }
    }

//    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
//        if (payloads.isEmpty()) {
//            super.onBindViewHolder(holder, position, payloads)
//        } else {
//            if (payloads[0] == true) {
//                Log.d("vunh", "payloads")
////                holder.bind(getItem(position), currentTime)
//            }
//        }
//    }
}