package com.riocallos.githubusers.features.main

import android.content.Context
import android.graphics.*
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.riocallos.githubusers.R
import com.riocallos.githubusers.base.BaseAdapter
import com.riocallos.githubusers.base.BaseViewHolder
import com.riocallos.githubusers.databinding.ItemUserBinding
import com.riocallos.githubusers.domain.models.User

class MainAdapter constructor(context: Context) : BaseAdapter<User>(context, BaseDiffCallback()) {

    internal inner class ItemUserViewHolder(private val binding: ItemUserBinding) :
            BaseViewHolder<User>(binding.root) {
        override fun bind(item: User, position: Int) {
            binding.user = item

            if(adapterPosition != 0 && (adapterPosition + 1) % 4 == 0) {

                val negative = floatArrayOf(
                    -1.0f,     .0f,     .0f,    .0f,  255.0f,
                    .0f,   -1.0f,     .0f,    .0f,  255.0f,
                    .0f,     .0f,   -1.0f,    .0f,  255.0f,
                    .0f,     .0f,     .0f,   1.0f,     .0f
                )
                binding.image.colorFilter = ColorMatrixColorFilter(negative)
                binding.image.borderColor = Color.WHITE

            } else {

                binding.image.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                binding.image.borderColor = Color.BLACK
            }

            binding.notes.visibility = (context as MainActivity).hasNote(item.username)

            binding.executePendingBindings()
        }
    }

    override fun createItemHolder(viewGroup: ViewGroup, itemType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemUserBinding>(
            layoutInflater,
            R.layout.item_user,
            viewGroup,
            false
        )
        return ItemUserViewHolder(binding)
    }

    override fun bindItemViewHolder(viewHolder: RecyclerView.ViewHolder, data: User, position: Int) {
        (viewHolder as ItemUserViewHolder)
                .bind(
                    data,
                    position
                )
    }
}