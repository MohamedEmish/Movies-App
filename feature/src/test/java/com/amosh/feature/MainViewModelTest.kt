package com.amosh.feature


//import com.google.common.truth.Truth
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.amosh.common.Resource
import com.amosh.domain.entity.ListType
import com.amosh.domain.entity.SortBy
import com.amosh.domain.useCase.AddToFavoriteUseCase
import com.amosh.domain.useCase.GetFavoriteListUseCase
import com.amosh.domain.useCase.GetMoviesListUseCase
import com.amosh.domain.useCase.RemoveFromFavoriteUseCase
import com.amosh.feature.mapper.MovieDomainUiMapper
import com.amosh.feature.ui.MainViewModel
import com.amosh.feature.ui.contract.MainContract
import com.amosh.feature.utils.MainCoroutineRule
import com.amosh.feature.utils.TestDataGenerator
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.ExperimentalTime


@ExperimentalTime
@ExperimentalCoroutinesApi
@SmallTest
class MainViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    @MockK
    private lateinit var getMovieUseCase: GetMoviesListUseCase

    @MockK
    private lateinit var getFavoriteListUseCase: GetFavoriteListUseCase

    @MockK
    private lateinit var addToFavoriteUseCase: AddToFavoriteUseCase

    @MockK
    private lateinit var removeFromFavoriteUseCase: RemoveFromFavoriteUseCase

    private val movieMapper = MovieDomainUiMapper()

    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks
        // Create MainViewModel before every test
        mainViewModel = MainViewModel(
            savedStateHandle = savedStateHandle,
            getMoviesListUseCase = getMovieUseCase,
            getFavoriteListUseCase = getFavoriteListUseCase,
            addToFavoriteUseCase = addToFavoriteUseCase,
            removeFromFavoriteUseCase = removeFromFavoriteUseCase,
            movieMapper = movieMapper
        )
        mainViewModel.viewModelScope.launch {
            getFavoriteListUseCase.execute(ListType.FAVORITES)
        }
    }

    @Test
    fun test_fetch_movieItems_success() = runBlockingTest {

        val movieItems = TestDataGenerator.generateMovieItem()
        val movieFlow = flowOf(Resource.Success(listOf(movieItems)))

        // Given
        coEvery { getMovieUseCase.execute(any()) } returns movieFlow

        // When && Assertions
        mainViewModel.uiState.test {
            mainViewModel.setEvent(MainContract.Event.OnFetchMoviesList(ListType.ALL, SortBy.NONE))
            // Expect Resource.Idle from initial state
            Truth.assertThat(expectItem()).isEqualTo(
                MainContract.State(
                    movieState = MainContract.MovieState.Idle,
                    selectedMovie = null
                )
            )
            // Expect Resource.Loading
            Truth.assertThat(expectItem()).isEqualTo(
                MainContract.State(
                    movieState = MainContract.MovieState.Loading,
                    selectedMovie = null
                )
            )
            // Expect Resource.Success
            val expected = expectItem()
            val expectedData =
                (expected.movieState as MainContract.MovieState.Success).moviesList
            Truth.assertThat(expected).isEqualTo(
                MainContract.State(
                    movieState = MainContract.MovieState.Success(
                        listOf(
                            movieMapper.from(
                                movieItems
                            )
                        )
                    ),
                    selectedMovie = null
                )
            )
            Truth.assertThat(expectedData)
                .containsExactlyElementsIn(listOf(movieMapper.from(movieItems)))


            //Cancel and ignore remaining
            cancelAndIgnoreRemainingEvents()
        }


        // Then
        coVerify { getMovieUseCase.execute(any()) }
    }

    @Test
    fun test_fetch_movie_fail() = runBlockingTest {

        val movieErrorFlow = flowOf(Resource.Error(Exception("error string")))

        // Given
        coEvery { getMovieUseCase.execute(any()) } returns movieErrorFlow

        // When && Assertions (UiState)
        mainViewModel.uiState.test {
            // Call method inside of this scope
            mainViewModel.setEvent(MainContract.Event.OnFetchMoviesList(ListType.ALL, SortBy.NONE))
            // Expect Resource.Idle from initial state
            Truth.assertThat(expectItem()).isEqualTo(
                MainContract.State(
                    movieState = MainContract.MovieState.Idle,
                    selectedMovie = null
                )
            )
            // Expect Resource.Loading
            Truth.assertThat(expectItem()).isEqualTo(
                MainContract.State(
                    movieState = MainContract.MovieState.Loading,
                    selectedMovie = null
                )
            )
            // Cancel and ignore remaining
            cancelAndIgnoreRemainingEvents()
        }

        // When && Assertions (UiEffect)
        mainViewModel.effect.test {
            // Expect ShowError Effect
            val expected = expectItem()
            val expectedData = (expected as MainContract.Effect.ShowError).message
            Truth.assertThat(expected).isEqualTo(
                MainContract.Effect.ShowError("error string")
            )
            Truth.assertThat(expectedData).isEqualTo("error string")
            // Cancel and ignore remaining
            cancelAndIgnoreRemainingEvents()
        }


        // Then
        coVerify { getMovieUseCase.execute(any()) }
    }


    @Test
    fun test_fetch_favorite_movieItems_success() = runBlockingTest {

        val movieItems = TestDataGenerator.generateMovieItem()
        val movieFlow = flowOf(Resource.Success(listOf(movieItems)))

        // Given
        coEvery { getFavoriteListUseCase.execute(any()) } returns movieFlow

        // When && Assertions
        mainViewModel.uiState.test {
            mainViewModel.setEvent(MainContract.Event.OnFetchMoviesList(ListType.FAVORITES, SortBy.NONE))
            // Expect Resource.Idle from initial state
            Truth.assertThat(expectItem()).isEqualTo(
                MainContract.State(
                    movieState = MainContract.MovieState.Idle,
                    selectedMovie = null
                )
            )
            // Expect Resource.Loading
            Truth.assertThat(expectItem()).isEqualTo(
                MainContract.State(
                    movieState = MainContract.MovieState.Loading,
                    selectedMovie = null
                )
            )
            // Expect Resource.Success
            val expected = expectItem()
            val expectedData =
                (expected.movieState as MainContract.MovieState.Success).moviesList
            Truth.assertThat(expected).isEqualTo(
                MainContract.State(
                    movieState = MainContract.MovieState.Success(
                        listOf(
                            movieMapper.from(
                                movieItems
                            )
                        )
                    ),
                    selectedMovie = null
                )
            )
            Truth.assertThat(expectedData)
                .containsExactlyElementsIn(listOf(movieMapper.from(movieItems)))


            //Cancel and ignore remaining
            cancelAndIgnoreRemainingEvents()
        }


        // Then
        coVerify { getFavoriteListUseCase.execute(any()) }
    }

    @Test
    fun test_fetch_favorite_movie_fail() = runBlockingTest {

        val movieErrorFlow = flowOf(Resource.Error(Exception("error string")))

        // Given
        coEvery { getFavoriteListUseCase.execute(any()) } returns movieErrorFlow

        // When && Assertions (UiState)
        mainViewModel.uiState.test {
            // Call method inside of this scope
            mainViewModel.setEvent(MainContract.Event.OnFetchMoviesList(ListType.FAVORITES, SortBy.NONE))
            // Expect Resource.Idle from initial state
            Truth.assertThat(expectItem()).isEqualTo(
                MainContract.State(
                    movieState = MainContract.MovieState.Idle,
                    selectedMovie = null
                )
            )
            // Expect Resource.Loading
            Truth.assertThat(expectItem()).isEqualTo(
                MainContract.State(
                    movieState = MainContract.MovieState.Loading,
                    selectedMovie = null
                )
            )
            // Cancel and ignore remaining
            cancelAndIgnoreRemainingEvents()
        }

        // When && Assertions (UiEffect)
        mainViewModel.effect.test {
            // Expect ShowError Effect
            val expected = expectItem()
            val expectedData = (expected as MainContract.Effect.ShowError).message
            Truth.assertThat(expected).isEqualTo(
                MainContract.Effect.ShowError("error string")
            )
            Truth.assertThat(expectedData).isEqualTo("error string")
            // Cancel and ignore remaining
            cancelAndIgnoreRemainingEvents()
        }


        // Then
        coVerify { getFavoriteListUseCase.execute(any()) }
    }


    @Test
    fun test_select_movie_item() = runBlockingTest {

        val movie = TestDataGenerator.generateMovieItem()

        // Given (no-op)

        // When && Assertions
        mainViewModel.uiState.test {
            // Call method inside of this scope
            // For more info, see https://github.com/cashapp/turbine/issues/19
            mainViewModel.setEvent(
                MainContract.Event.OnFetchMoviesDetails(
                    id = movieMapper.from(
                        movie
                    ).id
                )
            )
            // Expect Resource.Idle from initial state
            Truth.assertThat(expectItem()).isEqualTo(
                MainContract.State(
                    movieState = MainContract.MovieState.Idle,
                    selectedMovie  = null
                )
            )
            // Expect Resource.Success
            val expected = expectItem()
            val expectedData = expected.selectedMovie
            Truth.assertThat(expected).isEqualTo(
                MainContract.State(
                    movieState = MainContract.MovieState.Idle,
                    selectedMovie = movieMapper.from(movie)
                )
            )
            Truth.assertThat(expectedData).isEqualTo(movieMapper.from(movie))
            // Cancel and ignore remaining
            cancelAndIgnoreRemainingEvents()
        }


        // Then (no-op)
    }
}