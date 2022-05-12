package com.codegrow.feature.converter.main


//import com.google.common.truth.Truth
import androidx.lifecycle.SavedStateHandle
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.codegrow.domain.core.Resource
import com.codegrow.domain.usecase.ConvertUseCase
import com.codegrow.domain.usecase.GetSymbolsUseCase
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
class MainViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    @MockK
    private lateinit var getSymbolsUseCase: GetSymbolsUseCase

    @MockK
    private lateinit var convertUseCase: ConvertUseCase

    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks
        // Create MainViewModel before every test
        mainViewModel = MainViewModel(
            savedStateHandle = savedStateHandle,
            convertUseCase = convertUseCase,
            getSymbolsUseCase = getSymbolsUseCase
        )
    }

    @Test
    fun test_fetch_SymbolItems_success() = runBlockingTest {

        val symbolsItem = TestDataGenerator.generateSymbolItems()
        val symbolFlow = flowOf(Resource.Success(symbolsItem))

        // Given
        coEvery { getSymbolsUseCase.execute(any()) } returns symbolFlow

        // When && Assertions
        mainViewModel.uiState.test {
            mainViewModel.setEvent(MainContract.Event.GetSymbol)
            // Expect Resource.Idle from initial state
            Truth.assertThat(expectItem()).isEqualTo(
                MainContract.State(
                    mainState = MainContract.MainState.Idle,
                )
            )
            // Expect Resource.Loading
            Truth.assertThat(expectItem()).isEqualTo(
                MainContract.State(
                    mainState = MainContract.MainState.Loading
                )
            )
            // Expect Resource.Success
            val expected = expectItem()
            val expectedData = (expected.mainState as MainContract.MainState.Success).result
            Truth.assertThat(expected).isEqualTo(
                MainContract.State(
                    mainState = MainContract.MainState.Success(result = TestDataGenerator.generateSymbolList())
                )
            )

            Truth.assertThat(expectedData).containsExactlyElementsIn(TestDataGenerator.generateSymbolList())

            //Cancel and ignore remaining
            cancelAndIgnoreRemainingEvents()
        }




        // Then
        coVerify { getSymbolsUseCase.execute(any()) }
    }



    @Test
    fun test_fetch_symbol_fail() = runBlockingTest {

        val errorFlow = flowOf(Resource.Error("error string"))

        // Given
        coEvery { getSymbolsUseCase.execute(any()) } returns errorFlow

        // When && Assertions (UiState)
        mainViewModel.uiState.test {
            // Call method inside of this scope
            mainViewModel.setEvent(MainContract.Event.GetSymbol)
            // Expect Resource.Idle from initial state
            Truth.assertThat(expectItem()).isEqualTo(
                MainContract.State(
                    mainState = MainContract.MainState.Idle
                )
            )
            // Expect Resource.Loading
            Truth.assertThat(expectItem()).isEqualTo(
                MainContract.State(
                    mainState = MainContract.MainState.Loading
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
        coVerify { getSymbolsUseCase.execute(any()) }
    }


    @Test
    fun test_fetch_convert_success() = runBlockingTest {

        val resultItem = 100.0
        val resultFlow = flowOf(Resource.Success(resultItem))

        // Given
        coEvery { convertUseCase.execute(any()) } returns resultFlow

        // When && Assertions
        mainViewModel.uiState.test {

            mainViewModel.setEvent(MainContract.Event.Convert(10.0,"2022-05-10","2022-05-13"))

            // Expect Resource.Idle from initial state
            Truth.assertThat(expectItem()).isEqualTo(
                MainContract.State(
                    mainState = MainContract.MainState.Idle,
                )
            )

            // Expect Resource.Loading
            Truth.assertThat(expectItem()).isEqualTo(
                MainContract.State(
                    mainState = MainContract.MainState.Loading
                )
            )

            // Expect Resource.Success
            val expected = expectItem()
            val expectedData = (expected.mainState as MainContract.MainState.SuccessConvert).result
            Truth.assertThat(expected).isEqualTo(
                MainContract.State(
                    mainState = MainContract.MainState.SuccessConvert(result = resultItem)
                )
            )

            //Cancel and ignore remaining
            cancelAndIgnoreRemainingEvents()
        }

        // Then
        coVerify { convertUseCase.execute(any()) }
    }



    @Test
    fun test_fetch_convert_fails() = runBlockingTest {


        val errorFlow = flowOf(Resource.Error("error string"))

        // Given
        coEvery { convertUseCase.execute(any()) } returns errorFlow

        // When && Assertions (UiState)
        mainViewModel.uiState.test {
            // Call method inside of this scope
            mainViewModel.setEvent(MainContract.Event.Convert(100.0,"2022-05-10","2022-05-13"))
            // Expect Resource.Idle from initial state
            Truth.assertThat(expectItem()).isEqualTo(
                MainContract.State(
                    mainState = MainContract.MainState.Idle
                )
            )
            // Expect Resource.Loading
            Truth.assertThat(expectItem()).isEqualTo(
                MainContract.State(
                    mainState = MainContract.MainState.Loading
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
        coVerify { convertUseCase.execute(any()) }
    }


}