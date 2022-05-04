package com.yks.videogamesapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yks.videogamesapp.databinding.GameItemBinding
import com.yks.videogamesapp.model.GamesResult
import com.yks.videogamesapp.utils.download

class GamesRecyclerAdapter: RecyclerView.Adapter<GamesRecyclerAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<GamesResult>() {
        override fun areItemsTheSame(oldItem: GamesResult, newItem: GamesResult): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: GamesResult, newItem: GamesResult): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, differCallback)

    fun submitList(list: List<GamesResult>) {
        differ.submitList(list)
    }

    private fun getItem(position: Int): GamesResult {
        return differ.currentList[position]
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
        val game = getItem(position)

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

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ViewHolder(val binding: GameItemBinding):
        RecyclerView.ViewHolder(binding.root)

    private var onItemClickListener: ((GamesResult) -> Unit)? = null

    fun setOnItemClickListener(listener: (GamesResult) -> Unit) { onItemClickListener = listener }

}