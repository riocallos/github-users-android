package com.riocallos.githubusers.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.riocallos.githubusers.database.AppDatabase
import javax.inject.Inject

abstract class BaseAdapter<T>(protected val context: Context, diffCallback: DiffUtil.ItemCallback<T>) :
        ListAdapter<T, RecyclerView.ViewHolder>(diffCallback) {

    class BaseDiffCallback<T> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    var itemClickListener: OnItemClickListener<T>? = null
    protected var layoutInflater: LayoutInflater = LayoutInflater.from(context)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            bindItemViewHolder(holder, item, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holder = createItemHolder(parent, viewType)
        holder
                .itemView
                .setOnClickListener {
                    itemClickListener?.let { listener ->
                        getItem(holder.adapterPosition)?.let { item ->
                            listener.onItemClick(
                                    holder.itemView,
                                    item,
                                    holder.adapterPosition
                            )
                        }
                    }
                }
        return holder
    }

    protected abstract fun createItemHolder(viewGroup: ViewGroup, itemType: Int): RecyclerView.ViewHolder
    protected abstract fun bindItemViewHolder(viewHolder: RecyclerView.ViewHolder, data: T, position: Int)
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * clear adapter data
     */
    fun clear() {
        submitList(listOf<T>())
        notifyDataSetChanged()
    }
}