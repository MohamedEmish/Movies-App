package com.amosh.feature.ui.contract

import com.amosh.base.UiEffect
import com.amosh.base.UiEvent
import com.amosh.base.UiState
import com.amosh.domain.entity.ListType
import com.amosh.domain.entity.SortBy
import com.amosh.feature.model.MovieUiModel

/**
 * Contract of Main Screen
 */
class MainContract {

    sealed class Event : UiEvent {
        data class OnFetchMoviesList(val type: ListType, val sortBy: SortBy): Event()
        data class OnFetchMoviesDetails(val id: Int) : Event()
    }

    data class State(
        val movieState: MovieState,
        val selectedMovie: MovieUiModel? = null
    ) : UiState

    sealed class MovieState {
        object Idle : MovieState()
        object Loading : MovieState()
        data class Success(val moviesList: List<MovieUiModel>) : MovieState()
    }

    sealed class Effect : UiEffect {
        data class ShowError(val message: String?) : Effect()
    }
}