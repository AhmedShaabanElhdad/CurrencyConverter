package com.codegrow.feature.converter.details

import androidx.lifecycle.SavedStateHandle
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.codegrow.domain.core.Resource
import com.codegrow.domain.usecase.GetHistoricalSearchUseCase
import com.codegrow.feature.core.MainCoroutineRule
import com.codegrow.feature.core.TestDataGenerator
import com.google.common.truth.Truth
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
class DetailsViewModelTest{

        @get:Rule
        val mainCoroutineRule = MainCoroutineRule()

        @MockK
        private lateinit var savedStateHandle: SavedStateHandle

        @MockK
        private lateinit var getHistoricalSearchUseCase: GetHistoricalSearchUseCase


        private lateinit var detailsViewModel: DetailsViewModel

        @Before
        fun setUp() {
            MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks
            // Create MainViewModel before every test
            detailsViewModel = DetailsViewModel(
                savedStateHandle = savedStateHandle,
                getHistoricalSearchUseCase = getHistoricalSearchUseCase
            )
        }

        @Test
        fun test_fetch_Details_success() = runBlockingTest {

            val ratesItem = TestDataGenerator.generateRateItems()
            val rateFlow = flowOf(Resource.Success(ratesItem))

            // Given
            coEvery { getHistoricalSearchUseCase.execute(any()) } returns rateFlow

            // When && Assertions
            detailsViewModel.uiState.test {
                detailsViewModel.setEvent(DetailsContract.Event.GetHistorical("",""))
                // Expect Resource.Idle from initial state
                Truth.assertThat(expectItem()).isEqualTo(
                    DetailsContract.State(
                        mainState = DetailsContract.DetailsState.Idle,
                    )
                )
                // Expect Resource.Loading
                Truth.assertThat(expectItem()).isEqualTo(
                    DetailsContract.State(
                        mainState = DetailsContract.DetailsState.Loading
                    )
                )
                // Expect Resource.Success
                val expected = expectItem()
                val expectedData = (expected.mainState as DetailsContract.DetailsState.Success).result
                Truth.assertThat(expected).isEqualTo(
                    DetailsContract.State(
                        mainState = DetailsContract.DetailsState.Success(result = TestDataGenerator.generateRatesList())
                    )
                )

                Truth.assertThat(expectedData).containsExactlyElementsIn(TestDataGenerator.generateRatesList())

                //Cancel and ignore remaining
                cancelAndIgnoreRemainingEvents()
            }




            // Then
            coVerify { getHistoricalSearchUseCase.execute(any()) }
        }



        @Test
        fun test_fetch_symbol_fail() = runBlockingTest {

            val errorFlow = flowOf(Resource.Error("error string"))

            // Given
            coEvery { getHistoricalSearchUseCase.execute(any()) } returns errorFlow

            // When && Assertions (UiState)
            detailsViewModel.uiState.test {
                // Call method inside of this scope
                detailsViewModel.setEvent(DetailsContract.Event.GetHistorical("",""))
                // Expect Resource.Idle from initial state
                Truth.assertThat(expectItem()).isEqualTo(
                    DetailsContract.State(
                        mainState = DetailsContract.DetailsState.Idle
                    )
                )
                // Expect Resource.Loading
                Truth.assertThat(expectItem()).isEqualTo(
                    DetailsContract.State(
                        mainState = DetailsContract.DetailsState.Loading
                    )
                )
                // Cancel and ignore remaining
                cancelAndIgnoreRemainingEvents()
            }

            // When && Assertions (UiEffect)
            detailsViewModel.effect.test {
                // Expect ShowError Effect
                val expected = expectItem()
                val expectedData = (expected as DetailsContract.Effect.ShowError).message
                Truth.assertThat(expected).isEqualTo(
                    DetailsContract.Effect.ShowError("error string")
                )

                Truth.assertThat(expectedData).isEqualTo("error string")
                // Cancel and ignore remaining
                cancelAndIgnoreRemainingEvents()
            }


            // Then
            coVerify { getHistoricalSearchUseCase.execute(any()) }
        }
}