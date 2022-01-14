package com.amosh.feature.ui.main

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.amosh.base.BaseFragment
import com.amosh.domain.entity.ListType
import com.amosh.domain.entity.SortBy
import com.amosh.feature.databinding.FragmentMainBinding
import com.amosh.feature.ui.contract.MainContract
import com.amosh.feature.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


/**
 * Main Fragment
 */
@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>() {

    private val viewModel: MainViewModel by activityViewModels()
    private val adapter: MovieAdapter by lazy {
        MovieAdapter { movie ->
            viewModel.setEvent(
                MainContract.Event.OnFetchMoviesDetails(
                    movie?.id ?: return@MovieAdapter
                )
            )
            val action = MainFragmentDirections.actionMainFragmentToDetailFragment(movie)
            findNavController().navigate(action)
        }
    }

    override val bindLayout: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMainBinding
        get() = FragmentMainBinding::inflate

    override fun prepareView(savedInstanceState: Bundle?) {
        binding.rvWeather.adapter = adapter
        initObservers()
        viewModel.setEvent(MainContract.Event.OnFetchMoviesList(ListType.ALL, SortBy.NONE))
    }

    /**
     * Initialize Observers
     */
    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (val state = it.movieState) {
                        is MainContract.MovieState.Idle -> {
                            binding.loadingPb.isVisible = false
                        }
                        is MainContract.MovieState.Loading -> {
                            binding.loadingPb.isVisible = true
                        }
                        is MainContract.MovieState.Success -> {
                            val data = state.moviesList
                            adapter.submitList(data)
                            binding.loadingPb.isVisible = false
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.effect.collect {
                    when (it) {
                        is MainContract.Effect.ShowError -> {
                            val msg = it.message
                        }
                    }
                }
            }
        }
    }
}