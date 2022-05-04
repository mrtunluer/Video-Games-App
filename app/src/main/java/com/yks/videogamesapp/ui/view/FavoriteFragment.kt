package com.yks.videogamesapp.ui.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.yks.videogamesapp.R
import com.yks.videogamesapp.adapter.FavoritePagingAdapter
import com.yks.videogamesapp.adapter.FooterAdapter
import com.yks.videogamesapp.databinding.FragmentFavoriteBinding
import com.yks.videogamesapp.model.LikedGames
import com.yks.videogamesapp.ui.viewmodel.FavoriteFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite) {
    private lateinit var binding: FragmentFavoriteBinding
    private val viewModel by viewModels<FavoriteFragmentViewModel>()
    private val favoritePagingAdapter = FavoritePagingAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriteBinding.bind(view)
        init()
        loadStateListener()
        observeLikedGames()

        favoritePagingAdapter.setOnItemClickListener {
            goDetailsFragment(it)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            favoritePagingAdapter.refresh()
        }

        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.toolBar.setOnClickListener {
            binding.recyclerView.smoothScrollToPosition(0)
        }

    }

    private fun init() {
        binding.swipeRefreshLayout.setColorSchemeColors(Color.WHITE)
        binding.swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.rgb(0,0,0))

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            adapter = favoritePagingAdapter.withLoadStateFooter(FooterAdapter{
                favoritePagingAdapter.retry() })
        }
    }

    private fun loadStateListener(){
        favoritePagingAdapter.addLoadStateListener { combinedLoadStates ->
            visibilityOfViewComponent(combinedLoadStates)
        }
    }

    private fun visibilityOfViewComponent(combinedLoadStates: CombinedLoadStates){
        binding.swipeRefreshLayout.isRefreshing = combinedLoadStates.source.refresh is LoadState.Loading
        binding.errorTxt.isVisible = combinedLoadStates.source.refresh is LoadState.Error
    }

    private fun goDetailsFragment(likedGame: LikedGames){
        val id = bundleOf("id" to likedGame.id)
        findNavController().navigate(R.id.action_favoriteFragment_to_detailsFragment, id)
    }

    private fun observeLikedGames(){
        viewModel.likedGamesLiveData.observe(viewLifecycleOwner,{ likedGames ->
            likedGames?.let {
                lifecycleScope.launch {
                    favoritePagingAdapter.submitData(it)
                }
            }
        })
    }

}