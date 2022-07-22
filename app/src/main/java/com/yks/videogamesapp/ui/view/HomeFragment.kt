package com.yks.videogamesapp.ui.view

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.yks.videogamesapp.R
import com.yks.videogamesapp.adapter.FooterAdapter
import com.yks.videogamesapp.adapter.GamesPagingAdapter
import com.yks.videogamesapp.adapter.GamesRecyclerAdapter
import com.yks.videogamesapp.adapter.ViewPagerAdapter
import com.yks.videogamesapp.databinding.FragmentHomeBinding
import com.yks.videogamesapp.model.GamesResult
import com.yks.videogamesapp.ui.viewmodel.HomeFragmentViewModel
import com.yks.videogamesapp.utils.Constants.PAGE_SIZE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding()
    private val viewModel by viewModels<HomeFragmentViewModel>()
    private val viewPagerAdapter = ViewPagerAdapter()
    private val gamesPagingAdapter = GamesPagingAdapter()
    private val gamesSearchingAdapter = GamesRecyclerAdapter()
    private val viewPagerGamesList = arrayListOf<GamesResult>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        loadStateListener()
        observeGamesResult()

        binding.swipeRefreshLayout.setOnRefreshListener {
            getData()
        }

        gamesPagingAdapter.setOnItemClickListener {
            goDetailsFragment(it)
        }

        gamesSearchingAdapter.setOnItemClickListener {
            goDetailsFragment(it)
        }

        viewPagerAdapter.setOnItemClickListener {
            goDetailsFragment(it)
        }

        search()

    }

    private fun goDetailsFragment(gameResult: GamesResult){
        val id = bundleOf("id" to gameResult.id)
        findNavController().navigate(R.id.action_homeFragment_to_detailsFragment, id)
    }

    private fun search(){
        binding.searchTxt.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do Nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do Nothing
            }

            override fun afterTextChanged(s: Editable?) {
                if (s == null || s.isEmpty()){
                    visibilityOfPaging()
                }else if (s.toString().length >= 3){
                    visibilityOfSearch()
                    filter(s)
                }
            }

        })
    }

    private fun visibilityOfSearch(){
        binding.searchRecyclerView.visibility = View.VISIBLE
        binding.constraintViewPager.visibility = View.GONE
        binding.pagingRecyclerView.visibility = View.GONE
    }

    private fun visibilityOfPaging(){
        binding.searchRecyclerView.visibility = View.GONE
        binding.constraintViewPager.visibility = View.VISIBLE
        binding.pagingRecyclerView.visibility = View.VISIBLE
    }

    private fun filter(text: Editable?){
        val filteredList = arrayListOf<GamesResult>()
        for(item in gamesPagingAdapter.snapshot().toMutableList()){
            if (item != null) {
                if(item.name.lowercase().contains(text.toString().lowercase())){
                    filteredList.add(item)
                }
            }
        }
        gamesSearchingAdapter.submitList(filteredList)
        isResultExist(filteredList.size)
    }

    private fun getData(){
        viewModel.getGamesResult()
    }

    private fun isResultExist(size: Int){
        if (size == 0){
            binding.noResultTxt.visibility = View.VISIBLE
        }else{
            binding.noResultTxt.visibility = View.GONE
        }
    }

    private fun visibilityOfViewComponent(combinedLoadStates: CombinedLoadStates){
        binding.swipeRefreshLayout.isRefreshing = combinedLoadStates.source.refresh is LoadState.Loading
        binding.errorTxt.isVisible = combinedLoadStates.source.refresh is LoadState.Error
    }

    private fun loadStateListener(){
        gamesPagingAdapter.addLoadStateListener { combinedLoadStates ->
            visibilityOfViewComponent(combinedLoadStates)
            setFirstThreeItemsToViewPager(gamesPagingAdapter.snapshot())
        }
    }

    private fun setFirstThreeItemsToViewPager(snapshot: ItemSnapshotList<GamesResult>){
        if(snapshot.items.size in 1..PAGE_SIZE){
            viewPagerGamesList.clear()
            for(i in 0..2){ // ilk 3 item viewpager listesine eklenir.
                gamesPagingAdapter.snapshot()[i]?.let { viewPagerGamesList.add(it) }
            }
            viewPagerAdapter.submitList(viewPagerGamesList)
        }
    }

    private fun observeGamesResult(){
        viewModel.gamesResult.observe(viewLifecycleOwner, { gamesResult ->
            gamesResult?.let {
                gamesPagingAdapter.submitData(lifecycle,it)
            }
        })
    }

    private fun init() {
        binding.swipeRefreshLayout.setColorSchemeColors(Color.WHITE)
        binding.swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.rgb(0,0,0))

        binding.viewPager.apply {
            adapter = viewPagerAdapter
            binding.wormDotsIndicator.attachTo(this)
        }

        binding.searchRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = gamesSearchingAdapter
        }

        binding.pagingRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = gamesPagingAdapter.withLoadStateFooter(FooterAdapter{
                gamesPagingAdapter.retry() })
        }

    }

}