package com.amosh.feature.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.amosh.base.BaseViewModel
import com.amosh.common.Constants
import com.amosh.common.Mapper
import com.amosh.common.Resource
import com.amosh.common.addIfNotExist
import com.amosh.domain.entity.ListType
import com.amosh.domain.entity.MovieEntity
import com.amosh.domain.entity.SortBy
import com.amosh.domain.useCase.AddToFavoriteUseCase
import com.amosh.domain.useCase.GetFavoriteListUseCase
import com.amosh.domain.useCase.GetMoviesListUseCase
import com.amosh.domain.useCase.RemoveFromFavoriteUseCase
import com.amosh.feature.ui.contract.MainContract
import com.amosh.feature.model.MovieUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMoviesListUseCase: GetMoviesListUseCase,
    private val getFavoriteListUseCase: GetFavoriteListUseCase,
    private val addToFavoriteUseCase: AddToFavoriteUseCase,
    private val removeFromFavoriteUseCase: RemoveFromFavoriteUseCase,
    private val movieMapper: Mapper<MovieEntity, MovieUiModel>
) : BaseViewModel<MainContract.Event, MainContract.State, MainContract.Effect>() {

    private val listOfMovies: MutableList<MovieUiModel> = mutableListOf()
    val favoritesList: MutableList<MovieUiModel> = mutableListOf()
    var currentPage = 1

    override fun createInitialState(): MainContract.State {
        return MainContract.State(
            movieState = MainContract.MovieState.Idle,
            selectedMovie = null
        )
    }

    init {
        fetchFavoriteList()
    }

    override fun handleEvent(event: MainContract.Event) {
        when (event) {
            is MainContract.Event.OnFetchMoviesList -> fetchMoviesList(event.type, event.sortBy)
            is MainContract.Event.OnFetchMoviesDetails -> {
                val item = event.id
                setSelectedPost(movie = listOfMovies.find { movie -> movie.id == item })
            }
            is MainContract.Event.OnAddToFavorites -> addToFavorites(event.movie)
            is MainContract.Event.OnRemoveFromFavorites -> removeFromFavorite(event.movie)
            is MainContract.Event.OnSortCurrentList -> sortCurrentList(event.type, event.sort)
        }
    }

    private fun sortCurrentList(type: ListType, sortBy: SortBy) {
        setState {
            copy(
                movieState = MainContract.MovieState.Success(
                    moviesList = when (sortBy) {
                        SortBy.MOST_POPULAR ->
                            when (type) {
                                ListType.FAVORITES -> favoritesList
                                else -> listOfMovies.toList()
                            }.sortedBy { movie -> movie.popularity }
                                .reversed()
                        SortBy.HIGHEST_RATE -> when (type) {
                            ListType.FAVORITES -> favoritesList
                            else -> listOfMovies.toList()
                        }.sortedBy { movie -> movie.vote_average }
                            .reversed()
                        else -> when (type) {
                            ListType.FAVORITES -> favoritesList
                            else -> listOfMovies.toList()
                        }
                    }
                )
            )
        }
    }

    /**
     * Un favorite a movies
     */
    private fun removeFromFavorite(movie: MovieUiModel?) = viewModelScope.launch {
        removeFromFavoriteUseCase.execute(movieMapper.to(movie))
            .collect {
                when (it) {
                    is Resource.Loading -> Unit
                    is Resource.Empty -> Unit
                    is Resource.Error -> Unit
                    is Resource.Success -> {
                        fetchFavoriteList()
                    }
                }
            }
    }

    /**
     * Favorite a movies
     */
    private fun addToFavorites(movie: MovieUiModel?) = viewModelScope.launch {
        addToFavoriteUseCase.execute(movieMapper.to(movie))
            .collect {
                when (it) {
                    is Resource.Loading -> Unit
                    is Resource.Empty -> Unit
                    is Resource.Error -> Unit
                    is Resource.Success -> {
                        fetchFavoriteList()
                    }
                }
            }
    }

    /**
     * Fetch movies
     */
    private fun fetchMoviesList(type: ListType, sortBy: SortBy) =
        viewModelScope.launch {
            getMoviesListUseCase.execute(
                mapOf(
                    Constants.TYPE to type.name,
                    Constants.PAGE to currentPage.toString()
                )
            )
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
                            if (type == ListType.ALL) {
                                currentPage += 1
                                moviesList.forEach { movie ->
                                    listOfMovies.addIfNotExist(movie)
                                }
                            }

                            setState {
                                copy(
                                    movieState = MainContract.MovieState.Success(
                                        moviesList = when (sortBy) {
                                            SortBy.MOST_POPULAR ->
                                                when (type) {
                                                    ListType.FAVORITES -> moviesList
                                                    else -> listOfMovies.toList()
                                                }.sortedBy { movie -> movie.popularity }
                                                    .reversed()
                                            SortBy.HIGHEST_RATE -> when (type) {
                                                ListType.FAVORITES -> moviesList
                                                else -> listOfMovies.toList()
                                            }.sortedBy { movie -> movie.vote_average }
                                                .reversed()
                                            else -> when (type) {
                                                ListType.FAVORITES -> moviesList
                                                else -> listOfMovies.toList()
                                            }
                                        }
                                    )
                                )
                            }
                        }
                    }
                }
        }


    /**
     * Fetch favorite movies
     */
    private fun fetchFavoriteList() = viewModelScope.launch {
        getFavoriteListUseCase.execute(ListType.FAVORITES)
            .collect {
                when (it) {
                    is Resource.Loading -> Unit
                    is Resource.Empty -> Unit
                    is Resource.Error -> Unit
                    is Resource.Success -> {
                        val moviesList = movieMapper.fromList(it.data)
                        favoritesList.clear()
                        favoritesList.addAll(moviesList)
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