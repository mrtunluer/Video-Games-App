package com.yks.videogamesapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yks.videogamesapp.databinding.ViewPagerItemBinding
import com.yks.videogamesapp.model.GamesResult
import com.yks.videogamesapp.utils.download

class ViewPagerAdapter: RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

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

    inner class ViewHolder(val binding: ViewPagerItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ViewPagerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val context = holder.itemView.context
        val item = getItem(position)

        holder.apply {
            binding.imageView.download(context, item.backgroundImage)

            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(item)
                }
            }

        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((GamesResult) -> Unit)? = null

    fun setOnItemClickListener(listener: (GamesResult) -> Unit) { onItemClickListener = listener }

}