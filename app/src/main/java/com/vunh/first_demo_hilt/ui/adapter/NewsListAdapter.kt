package com.vunh.first_demo_hilt.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.models.News
import com.vunh.first_demo_hilt.utils.ConstantsDateType
import com.vunh.first_demo_hilt.utils.extension.*
import java.util.*

class NewsListAdapter : ListAdapter<News, NewsListAdapter.ViewHolder>(Callback()) {

    lateinit var binding: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged", "ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
//        holder.itemView.setOnClickListener {
//            AppUtils.setAnimationClickItem(it)
////            listener.onItem(item)
//        }
        holder.bind(item)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val timeTv: TextView = itemView.findViewById(R.id.item_news_time)
        private val titleTv: TextView = itemView.findViewById(R.id.item_news_title)
        private val contentTv: TextView = itemView.findViewById(R.id.item_news_content)

        fun bind(item: News) = with(itemView) {
            timeTv.text = item.time.toStringDate(format = "hh:mm")
            titleTv.text = item.title
            contentTv.text = item.content
        }
    }

    override fun submitList(list: MutableList<News>?) {
        super.submitList(ArrayList(list ?: arrayListOf()))
    }

    class Callback : DiffUtil.ItemCallback<News>() {
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
        }
    }

}