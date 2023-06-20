package com.vunh.first_demo_hilt.ui.adapter.base

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class GeneralRecyclerViewAdapter <T : Any, VM : ViewDataBinding> (
    @LayoutRes val layoutID: Int,
    private val bindingInterface: GeneralRecyclerBindingInterface<VM, T>,
    private val clickListener: GeneralClickListener<T>? = null
): ListAdapter<T, GeneralRecyclerViewAdapter.ViewHolder>(GeneralDiffUtilCallback()) {
    var selectionTracker: SelectionTracker<Long>? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, layoutID, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(
            item,
            bindingInterface,
            clickListener,
            position,
            selectionTracker?.isSelected(position.toLong()) ?: false
        )
    }

    class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun <T : Any, VM : ViewDataBinding> bind(
            item: T,
            bindingInterface: GeneralRecyclerBindingInterface<VM, T>,
            clickListener: GeneralClickListener<T>?,
            position: Int,
            b: Boolean
        ) {
            bindingInterface.bindData(binding as VM, item, clickListener, position)
            itemView.isActivated = b
        }

        fun getItemDetails() = object :
            ItemDetailsLookup.ItemDetails<Long>() {
            override fun getPosition() = bindingAdapterPosition
            override fun getSelectionKey() = itemId
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class GeneralDiffUtilCallback<T : Any> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem.toString() == newItem.toString()
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }
    }
}

interface GeneralRecyclerBindingInterface <VM : ViewDataBinding, T : Any> {
    fun bindData(binder: VM, model: T, clickListener: GeneralClickListener<T>?, position: Int)
}

class GeneralClickListener<T : Any>(private val clickListener: (T) -> Unit) {
    fun onClick(data: T) = clickListener(data)
}

class GeneralItemKeyProvider(private val recyclerView: RecyclerView) :
    ItemKeyProvider<Long>(SCOPE_CACHED) {
    override fun getKey(position: Int): Long? {
        return recyclerView.adapter?.getItemId(position)
    }

    override fun getPosition(key: Long): Int {
        return recyclerView.findViewHolderForItemId(key)?.layoutPosition ?: RecyclerView.NO_POSITION
    }
}

class GeneralItemLookUp(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y)
        if (view != null) {
            return (recyclerView.getChildViewHolder(view) as GeneralRecyclerViewAdapter.ViewHolder).getItemDetails()
        }
        return null
    }
}