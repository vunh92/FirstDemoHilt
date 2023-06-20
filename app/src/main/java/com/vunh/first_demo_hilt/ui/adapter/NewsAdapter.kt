package com.vunh.first_demo_hilt.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vunh.first_demo_hilt.R
import com.vunh.first_demo_hilt.models.News
import com.vunh.first_demo_hilt.models.NewsStickyHeader

class NewsAdapter(
    private var dataList: MutableList<NewsStickyHeader>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), StickHeaderItemDecoration.StickyHeaderInterface{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> HeaderViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.header_news, parent, false)
            )
            else -> ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_news_list, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                holder.bindData(dataList[position].listNews)
            }
            is HeaderViewHolder -> {
                holder.bindData(dataList[position].headerNews.toString())
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return dataList[position].viewType
    }

    override fun getItemCount(): Int {
        return dataList.size
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
        return R.layout.header_news
    }

    override fun bindHeaderData(header: View?, headerPosition: Int) {
        header?.findViewById<TextView>(R.id.header_news_tv)?.text = dataList[headerPosition].headerNews.toString()
    }

    override fun isHeader(itemPosition: Int): Boolean {
        return dataList[itemPosition].viewType == 1
    }

    internal class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var headerNewsTv = itemView.findViewById<TextView>(R.id.header_news_tv)

        fun bindData(title: String) {
            headerNewsTv.text = title
        }
    }

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var itemNewsRv = itemView.findViewById<RecyclerView>(R.id.item_news_rv)
        private lateinit var newsListAdapter: NewsListAdapter

        fun bindData(list: MutableList<News>) {
            newsListAdapter = NewsListAdapter()
            itemNewsRv.apply {
                adapter = newsListAdapter
                layoutManager = LinearLayoutManager(itemView.context)
                newsListAdapter.submitList(list)
            }
        }
    }
}