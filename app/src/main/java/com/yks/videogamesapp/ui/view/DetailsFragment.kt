package com.yks.videogamesapp.ui.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.yks.videogamesapp.R
import com.yks.videogamesapp.databinding.FragmentDetailsBinding
import com.yks.videogamesapp.model.GameDetails
import com.yks.videogamesapp.model.LikedGames
import com.yks.videogamesapp.ui.viewmodel.DetailsFragmentViewModel
import com.yks.videogamesapp.utils.Status
import com.yks.videogamesapp.utils.download
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private lateinit var binding: FragmentDetailsBinding
    private val viewModel by viewModels<DetailsFragmentViewModel>()
    private var isLiked: Boolean? = null
    private lateinit var likedGames: LikedGames

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)
        init()
        observeGameDetails()
        observeLikeBtn()

        binding.progressLayout.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getGameDetails()
        }

        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.likeBtn.setOnClickListener {
            isLiked?.let { likeItBefore ->
                if (likeItBefore)
                    dislikeGame()
                else
                    likeGame(likedGames)
            }
        }

    }

    private fun init() {
        binding.progressLayout.swipeRefreshLayout.setColorSchemeColors(Color.WHITE)
        binding.progressLayout.swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.rgb(0,0,0))
    }

    private fun likeGame(likedGame: LikedGames){
        viewModel.insertLikedGame(likedGame)
    }

    private fun dislikeGame(){
        viewModel.deleteLikeGame()
    }

    private fun observeLikeBtn() {
        lifecycleScope.launchWhenStarted {
            viewModel.likeBtnState.collect {
                when(it.status){
                    Status.SUCCESS -> {
                        it.data?.let {
                            onSuccessToLikeBtn()
                        }
                    }
                    Status.ERROR -> {
                        onError()
                    }
                    Status.EMPTY -> {
                        onEmptyToLikeBtn()
                    }
                }
            }
        }
    }

    private fun observeGameDetails(){
        viewModel.gameDetails.observe(viewLifecycleOwner, {gameDetails ->
            gameDetails?.let {
                when(it.status){
                    Status.SUCCESS -> {
                        it.data?.let {details ->
                            onSuccessToGameDetails(details)
                            if (details.id != null){
                                likedGames = LikedGames(details.id, System.currentTimeMillis(), details.name, details.released, details.rating, details.backgroundImage)
                            }
                        }
                    }
                    Status.ERROR -> {
                        onError()
                    }
                    Status.LOADING -> {
                        onLoading()
                    }
                }
            }
        })
    }

    private fun onSuccessToLikeBtn(){
        binding.likeBtn.setImageResource(R.drawable.ic_baseline_thumb_up_24_blue)
        isLiked = true
    }

    private fun onEmptyToLikeBtn(){
        binding.likeBtn.setImageResource(R.drawable.ic_baseline_thumb_up_24)
        isLiked = false
    }

    private fun onSuccessToGameDetails(details: GameDetails){
        binding.nameTxt.text = details.name

        details.description?.let {
            binding.decribeTxt.text = HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
        }

        if (details.metacritic == null){
            binding.metacriticRateTxt.visibility = View.GONE
        }else{
            binding.metacriticRateTxt.text = details.metacritic.toString()
        }
        binding.releaseTxt.text = details.released
        binding.imageView.download(requireContext(), details.backgroundImage)
        binding.progressLayout.root.isVisible = false
    }

    private fun onError(){
        binding.progressLayout.root.isVisible = true
        binding.progressLayout.errorTxt.isVisible = true
        binding.progressLayout.swipeRefreshLayout.isRefreshing = false
    }

    private fun onLoading(){
        binding.progressLayout.root.isVisible = true
        binding.progressLayout.errorTxt.isVisible = false
        binding.progressLayout.swipeRefreshLayout.isRefreshing = true
    }

}