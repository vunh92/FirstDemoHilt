package com.vunh.first_demo_hilt.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.models.DataStickHeader

class SimpleStickHeaderAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(), StickHeaderItemDecoration.StickyHeaderInterface{

    private val mData: MutableList<DataStickHeader> = arrayListOf()

    init {
        mData.add(DataStickHeader(1))
        mData.add(DataStickHeader(0))
        mData.add(DataStickHeader(0))
        mData.add(DataStickHeader(0))
        mData.add(DataStickHeader(0))
        mData.add(DataStickHeader(0))
        mData.add(DataStickHeader(2))
        mData.add(DataStickHeader(0))
        mData.add(DataStickHeader(0))
        mData.add(DataStickHeader(0))
        mData.add(DataStickHeader(0))
        mData.add(DataStickHeader(0))
        mData.add(DataStickHeader(0))
        mData.add(DataStickHeader(1))
        mData.add(DataStickHeader(0))
        mData.add(DataStickHeader(0))
        mData.add(DataStickHeader(0))
        mData.add(DataStickHeader(0))
        mData.add(DataStickHeader(0))
        mData.add(DataStickHeader(0))
        mData.add(DataStickHeader(2))
        mData.add(DataStickHeader(0))
        mData.add(DataStickHeader(0))
        mData.add(DataStickHeader(0))
        mData.add(DataStickHeader(0))
        mData.add(DataStickHeader(0))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.header_stick_header_1, parent, false))
            2 -> Header2ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.header_stick_header_2, parent, false))
            else -> ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_stick_header, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                holder.bindData(position)
            }
            is HeaderViewHolder -> {
                holder.bindData(position)
            }
            is Header2ViewHolder -> {
                holder.bindData(position)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return mData[position].viewType
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun getHeaderPositionForItem(itemPosition: Int): Int {
        var headerPosition = 0
        var item = itemPosition
        do {
            if (isHeader(item)) {
                headerPosition = item
                break
            }
            item -= 1
        } while (item >= 0)
        return headerPosition
    }

    override fun getHeaderLayout(headerPosition: Int): Int {
        return if (mData[headerPosition].viewType == 1)
            R.layout.header_stick_header_1
        else {
            R.layout.header_stick_header_2
        }
    }

    override fun bindHeaderData(header: View?, headerPosition: Int) {
        if (mData[headerPosition].viewType == 1)
            header?.findViewById<TextView>(R.id.tv_header)?.text = (headerPosition / 5).toString()
        else {
            header?.findViewById<TextView>(R.id.tv_header)?.text = (headerPosition / 5).toString()
        }
    }

    override fun isHeader(itemPosition: Int): Boolean {
        return mData[itemPosition].viewType == 1 || mData[itemPosition].viewType == 2
    }

    internal class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvHeader: TextView

        init {
            tvHeader = itemView.findViewById<TextView>(R.id.tv_header)
        }

        fun bindData(position: Int) {
            tvHeader.text = (position / 5).toString()
        }
    }

    internal class Header2ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var tvHeader: TextView

        init {
            tvHeader = itemView.findViewById<TextView>(R.id.tv_header)
        }

        fun bindData(position: Int) {
            tvHeader.text = (position / 5).toString()
        }
    }

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvRows: TextView

        init {
            tvRows = itemView.findViewById<TextView>(R.id.tv_row)
        }

        fun bindData(position: Int) {
            tvRows.text = "saber$position"
            (tvRows.parent as ViewGroup).setBackgroundColor(Color.parseColor("#ffffff"))
        }
    }
}