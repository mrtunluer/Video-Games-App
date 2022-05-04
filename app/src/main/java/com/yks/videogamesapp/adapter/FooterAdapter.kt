package com.yks.videogamesapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yks.videogamesapp.databinding.FooterItemBinding

class FooterAdapter(private val retry: () -> Unit): LoadStateAdapter<FooterAdapter.ViewHolder>() {

    class ViewHolder (val binding: FooterItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        return ViewHolder(
            FooterItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.binding.apply {
            retryImg.setOnClickListener { retry.invoke() }
            retryImg.isVisible = loadState !is LoadState.Loading
            circularProgress.isVisible = loadState is LoadState.Loading
        }
    }

}