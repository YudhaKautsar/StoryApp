package com.yudhakautsar.storyapp.base

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter<T : Any, VB : ViewBinding> :
    RecyclerView.Adapter<BaseAdapter.BaseViewHolder<VB>>() {

    private val items = mutableListOf<T>()

    var onItemClickListener: ((T, Int) -> Unit)? = null

    abstract fun getViewBinding(parent: ViewGroup): VB

    abstract fun onBindViewHolder(binding: VB, item: T, position: Int)

    open fun getDiffCallback(oldList: List<T>, newList: List<T>): DiffUtil.Callback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VB> {
        val binding = getViewBinding(parent)
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<VB>, position: Int) {
        val item = items[position]
        onBindViewHolder(holder.binding, item, position)

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(item, position)
        }
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newItems: List<T>) {
        val diffCallback = getDiffCallback(items, newItems)

        if (diffCallback != null) {
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            items.clear()
            items.addAll(newItems)
            diffResult.dispatchUpdatesTo(this)
        } else {
            items.clear()
            items.addAll(newItems)
            notifyDataSetChanged()
        }
    }

    fun getItem(position: Int): T = items[position]

    fun getItems(): List<T> = items.toList()

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    fun addItem(item: T) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun addItems(newItems: List<T>) {
        val startPosition = items.size
        items.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }

    fun removeItem(position: Int) {
        if (position in items.indices) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun updateItem(position: Int, item: T) {
        if (position in items.indices) {
            items[position] = item
            notifyItemChanged(position)
        }
    }

    class BaseViewHolder<VB : ViewBinding>(val binding: VB) :
        RecyclerView.ViewHolder(binding.root)
}

