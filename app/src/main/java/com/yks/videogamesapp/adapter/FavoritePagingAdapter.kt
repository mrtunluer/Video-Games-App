package com.yks.videogamesapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yks.videogamesapp.databinding.GameItemBinding
import com.yks.videogamesapp.model.LikedGames
import com.yks.videogamesapp.utils.download


class FavoritePagingAdapter: PagingDataAdapter<LikedGames, FavoritePagingAdapter.ViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<LikedGames>() {
        override fun areItemsTheSame(oldItem: LikedGames, newItem: LikedGames): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: LikedGames, newItem: LikedGames): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            GameItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val context = holder.itemView.context
        val game = getItem(position) ?: return

        holder.apply {

            binding.imageView.download(context, game.backgroundImage)
            binding.name.text = game.name
            binding.released.text = game.released
            binding.rating.text = game.rating.toString()

            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(game)
                }
            }

        }

    }

    inner class ViewHolder(val binding: GameItemBinding):
        RecyclerView.ViewHolder(binding.root)

    private var onItemClickListener: ((LikedGames) -> Unit)? = null

    fun setOnItemClickListener(listener: (LikedGames) -> Unit) { onItemClickListener = listener }

}