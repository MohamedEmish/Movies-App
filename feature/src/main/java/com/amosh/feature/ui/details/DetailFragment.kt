package com.amosh.feature.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.amosh.base.BaseFragment
import com.amosh.feature.R
import com.amosh.feature.databinding.FragmentDetailsBinding
import com.amosh.feature.model.MovieUiModel
import com.amosh.feature.ui.MainViewModel
import com.amosh.feature.ui.contract.MainContract
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailsBinding>() {

    private val viewModel: MainViewModel by activityViewModels()

    override val bindLayout: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDetailsBinding
        get() = FragmentDetailsBinding::inflate

    override fun prepareView(savedInstanceState: Bundle?) {
        initObservers()
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    setData(it.selectedMovie)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.effect.collect {
                    when (it) {
                        is MainContract.Effect.ShowError -> {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun setData(selectedMovie: MovieUiModel?) {
        with(binding) {
            ivImage.apply {
                val imageUrl = "https://image.tmdb.org/t/p/w300${selectedMovie?.poster_path}"
                Glide.with(this)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.ic_app_logo)
                    .placeholder(R.drawable.ic_app_logo)
                    .into(this)
            }

            tvTitle.apply {
                text = selectedMovie?.title
                setOnClickListener { requireActivity().onBackPressed() }
            }
            tvOverView.text = selectedMovie?.overview
            rbRate.rating = (selectedMovie?.vote_average ?: 0.0).toFloat().div(2)
            tvOriginalTitle.text = selectedMovie?.original_title
            tvTagLine.text = selectedMovie?.tagline
            btnFavChange.apply {
                val favItem =
                    viewModel.favoritesList.find { it.id == selectedMovie?.id && it.title == selectedMovie.title }
                setImageResource(
                    when (favItem) {
                        null -> R.drawable.ic_un_fav
                        else -> R.drawable.ic_fav
                    }
                )

                setOnClickListener {
                    setImageResource(
                        when (favItem) {
                            null -> R.drawable.ic_fav
                            else -> R.drawable.ic_un_fav
                        }
                    )
                    viewModel.setEvent(
                        when (favItem) {
                            null -> MainContract.Event.OnAddToFavorites(selectedMovie)
                            else -> MainContract.Event.OnRemoveFromFavorites(selectedMovie)
                        }
                    )
                }
            }
        }
    }

}