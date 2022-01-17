package com.amosh.domain

import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.amosh.common.Constants
import com.amosh.common.Resource
import com.amosh.domain.entity.ListType
import com.amosh.domain.repository.Repository
import com.amosh.domain.useCase.GetMoviesListUseCase
import com.google.common.truth.Truth
import com.amosh.domain.utils.MainCoroutineRule
import com.amosh.domain.utils.TestDataGenerator
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
@SmallTest
class GetMoviesListUseCaseTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var repository: Repository

    private lateinit var getMoviesListUseCase: GetMoviesListUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks
        getMoviesListUseCase = GetMoviesListUseCase(
            repository = repository,
            dispatcher = mainCoroutineRule.dispatcher
        )
    }

    @Test
    fun test_get_movie_success() = runBlockingTest {

        val movieItem = TestDataGenerator.generateMoviesItems()
        val movieFlow = flowOf(Resource.Success(movieItem))

        // Given
        coEvery { repository.getMoviesList(ListType.ALL, 1) } returns movieFlow

        // When & Assertions
        val result = getMoviesListUseCase.execute(
            mapOf(
                Constants.PAGE to "1",
                Constants.TYPE to ListType.ALL.name
            )
        )
        result.test {
            // Expect Resource.Success
            val expected = expectItem()
            val expectedData = (expected as Resource.Success).data
            Truth.assertThat(expected).isInstanceOf(Resource.Success::class.java)
            Truth.assertThat(expectedData).isEqualTo(movieItem)
            expectComplete()
        }

        // Then
        coVerify { repository.getMoviesList(ListType.ALL, 1) }

    }


    @Test
    fun test_get_movie_fail() = runBlockingTest {

        val errorFlow = flowOf(Resource.Error(Exception()))

        // Given
        coEvery { repository.getMoviesList(ListType.ALL, 1) } returns errorFlow

        // When & Assertions
        val result = getMoviesListUseCase.execute(
            mapOf(
                Constants.PAGE to "1",
                Constants.TYPE to ListType.ALL.name
            )
        )
        result.test {
            // Expect Resource.Error
            Truth.assertThat(expectItem()).isInstanceOf(Resource.Error::class.java)
            expectComplete()
        }

        // Then
        coVerify { repository.getMoviesList(ListType.ALL, 1) }

    }


    @Test
    fun test_get_movie_fail_pass_parameter_with_null() = runBlockingTest {

        val errorFlow = flowOf(Resource.Error(Exception()))

        // When & Assertions
        val result = getMoviesListUseCase.execute(null)
        result.test {
            // Expect Resource.Error
            Truth.assertThat(expectItem()).isInstanceOf(Resource.Error::class.java)
            expectComplete()
        }

        // Then

//        coVerify { repository.getMoviesList(ListType.ALL, 1) }

    }
}