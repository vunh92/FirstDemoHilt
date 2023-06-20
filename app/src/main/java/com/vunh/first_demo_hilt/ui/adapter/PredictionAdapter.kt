package com.vunh.first_demo_hilt.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.checkbox.MaterialCheckBox
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.models.Prediction
import com.vunh.first_demo_hilt.utils.AppUtils


class PredictionAdapter (
    private val isMultiple: Boolean = false,
    private var predictionSelected: Prediction? = null,
    listener: Listener,
) : RecyclerView.Adapter<PredictionAdapter.ViewHolder>() {

    private var prediction: Prediction? = null
    private var predictionList: MutableList<Prediction> = arrayListOf()
    private var mListener = listener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredictionAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_prediction, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return predictionList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: PredictionAdapter.ViewHolder, position: Int) {
        holder.nameTv.text = predictionList[position].name
        holder.checkedCkb.isChecked = if (isMultiple) predictionList[position].isChecked else predictionList[position].id == predictionSelected?.id
        holder.itemView.setOnClickListener {
            AppUtils.setAnimationClickItem(it)
            if (isMultiple) {
                predictionList[position].isChecked = !holder.checkedCkb.isChecked
            } else {
                prediction = predictionList[position]
            }
            notifyDataSetChanged()
            mListener.onItemPredictionClick(position, prediction, predictionList)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTv: TextView = itemView.findViewById(R.id.name_tv)
        val checkedCkb: MaterialCheckBox = itemView.findViewById(R.id.checked_ckb)
    }

    fun getItem(position: Int): Prediction {
        return predictionList[position]
    }

    fun clear() {
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    fun remove(r: Prediction) {
        val position: Int = predictionList.indexOf(r)
        if (position > -1) {
            predictionList.removeAt(position)
            notifyItemRemoved(position)
        }
    }


    fun add(r: Prediction) {
        predictionList.add(r)
        notifyItemInserted(predictionList.size - 1)
    }

    fun addAll(moveResults: List<Prediction>) {
        for (result in moveResults) {
            add(result)
        }
    }

    interface Listener {
        fun onItemPredictionClick(position: Int, prediction: Prediction?, list: MutableList<Prediction>)
    }
}