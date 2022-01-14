package com.amosh.feature.ui.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
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
import com.amosh.feature.ui.filterSheet.FilterSheetFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Main Fragment
 */
@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>() {

    private val viewModel: MainViewModel by activityViewModels()
    private var filterSheet: FilterSheetFragment? = null
    private var selectedType = ListType.ALL
    private var selectedSort = SortBy.NONE

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
        binding.rvMovies.adapter = adapter
        initObservers()
        getMoviesList()

        binding.ivFilter.setOnClickListener {
            filterSheet =
                FilterSheetFragment.newInstance(
                    object : FilterSheetFragment.OnActionsListener {
                        override fun onDoneListener(type: ListType, sortBy: SortBy) {
                            selectedSort = sortBy
                            selectedType = type
                            getMoviesList()
                            filterSheet?.dismiss()
                        }
                    },
                    selectedType,
                    selectedSort
                )
            filterSheet?.show(parentFragmentManager, FilterSheetFragment.TAG)

        }
    }

    private fun getMoviesList() =
        viewModel.setEvent(MainContract.Event.OnFetchMoviesList(selectedType, selectedSort))

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
                            binding.emptyState.isVisible = data.isNullOrEmpty()
                            binding.loadingPb.isVisible = false
                            binding.tvToolbar.text = when (selectedType){
                                ListType.FAVORITES -> "Favorites"
                                ListType.ALL -> "Movies"
                            }
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
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}