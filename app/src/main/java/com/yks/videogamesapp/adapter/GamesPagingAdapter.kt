package com.yks.videogamesapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yks.videogamesapp.databinding.GameItemBinding
import com.yks.videogamesapp.model.GamesResult
import com.yks.videogamesapp.utils.download

class GamesPagingAdapter: PagingDataAdapter<GamesResult, GamesPagingAdapter.ViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<GamesResult>() {
        override fun areItemsTheSame(oldItem: GamesResult, newItem: GamesResult): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: GamesResult, newItem: GamesResult): Boolean {
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

        visibilityOfItems(position, holder)

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

    private fun visibilityOfItems(position: Int, holder: ViewHolder){
        if (position in 0..2){
            holder.itemView.visibility = View.GONE
            holder.itemView.layoutParams = setLayoutParams(0,0)
        }else{
            holder.itemView.visibility = View.VISIBLE
            holder.itemView.layoutParams =
                setLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    private fun setLayoutParams(width: Int, height: Int) =
        RecyclerView.LayoutParams(width, height)

    inner class ViewHolder(val binding: GameItemBinding):
        RecyclerView.ViewHolder(binding.root)

    private var onItemClickListener: ((GamesResult) -> Unit)? = null

    fun setOnItemClickListener(listener: (GamesResult) -> Unit) { onItemClickListener = listener }

}