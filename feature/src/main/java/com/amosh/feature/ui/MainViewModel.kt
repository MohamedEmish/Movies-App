package com.amosh.feature.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.amosh.base.BaseViewModel
import com.amosh.common.Mapper
import com.amosh.common.Resource
import com.amosh.domain.entity.ListType
import com.amosh.domain.entity.MovieEntity
import com.amosh.domain.entity.SortBy
import com.amosh.domain.useCase.GetMoviesListUseCase
import com.amosh.feature.ui.contract.MainContract
import com.amosh.feature.model.MovieUiModel
import com.amosh.feature.ui.main.MovieAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMoviesListUseCase: GetMoviesListUseCase,
    private val movieMapper: Mapper<MovieEntity, MovieUiModel>
) : BaseViewModel<MainContract.Event, MainContract.State, MainContract.Effect>() {

    private val listOfMovies: MutableList<MovieUiModel> = mutableListOf()

    override fun createInitialState(): MainContract.State {
        return MainContract.State(
            movieState = MainContract.MovieState.Idle,
            selectedMovie = null
        )
    }

    override fun handleEvent(event: MainContract.Event) {
        when (event) {
            is MainContract.Event.OnFetchMoviesList -> fetchMoviesList(event.type, event.sortBy)
            is MainContract.Event.OnFetchMoviesDetails -> {
                val item = event.id
                setSelectedPost(movie = listOfMovies.find { movie -> movie.id == item })
            }
        }
    }

    /**
     * Fetch movies
     */
    private fun fetchMoviesList(type: ListType, sortBy: SortBy) = viewModelScope.launch {
        viewModelScope.launch {
            getMoviesListUseCase.execute(type)
                .onStart { emit(Resource.Loading) }
                .collect {
                    when (it) {
                        is Resource.Loading -> {
                            setState { copy(movieState = MainContract.MovieState.Loading) }
                        }
                        is Resource.Empty -> {
                            setState { copy(movieState = MainContract.MovieState.Idle) }
                        }
                        is Resource.Error -> {
                            setEffect { MainContract.Effect.ShowError(message = it.exception.message) }
                        }
                        is Resource.Success -> {
                            val moviesList = movieMapper.fromList(it.data)
                            listOfMovies.clear()
                            listOfMovies.addAll(moviesList)

                            setState {
                                copy(
                                    movieState = MainContract.MovieState.Success(
                                        moviesList = when (sortBy) {
                                            SortBy.MOST_POPULAR -> listOfMovies.toList()
                                                .sortedBy { movie -> movie.popularity }
                                            SortBy.HIGHEST_RATE -> listOfMovies.toList()
                                                .sortedBy { movie -> movie.vote_average }
                                            else -> listOfMovies.toList()
                                        }
                                    )
                                )
                            }
                        }
                    }
                }
        }
    }

    /**
     * Set selected post item
     */
    private fun setSelectedPost(movie: MovieUiModel?) {
        // Set State
        setState { copy(selectedMovie = movie) }
    }
}