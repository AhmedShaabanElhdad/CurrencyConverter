package com.codegrow.domain.usecase

import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.codegrow.domain.core.MainCoroutineRule
import com.codegrow.domain.core.Resource
import com.codegrow.domain.core.TestDataGenerator
import com.codegrow.domain.repository.SymbolRepository
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
@SmallTest
class GetSymbolsUseCaseTest{
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var repository: SymbolRepository

    private lateinit var getSymbolsUseCase: GetSymbolsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks
        getSymbolsUseCase = GetSymbolsUseCase(
            repository = repository
        )
    }

    @Test
    fun test_get_symbol_success() = runBlockingTest {

        val symbolItem = TestDataGenerator.generateSymbolItems()
        val flow = flowOf(Resource.Success(symbolItem))

        // Given
        coEvery { repository.getSymbol() } returns flow

        // When & Assertions
        val result = getSymbolsUseCase.execute("cairo")
        result.test {
            // Expect Resource.Success
            val expected = expectItem()
            val expectedData = (expected as Resource.Success).data
            Truth.assertThat(expected).isInstanceOf(Resource.Success::class.java)
            Truth.assertThat(expectedData).isEqualTo(symbolItem)
            expectComplete()
        }

        // Then
        coVerify { repository.getSymbol() }

    }




    @Test
    fun test_get_symbol_fail() = runBlockingTest {

        val errorFlow = flowOf(Resource.Error("Error"))

        // Given
        coEvery { repository.getSymbol() } returns errorFlow

        // When & Assertions
        val result = getSymbolsUseCase.execute(Any())
        result.test {
            // Expect Resource.Error
            Truth.assertThat(expectItem()).isInstanceOf(Resource.Error::class.java)
            expectComplete()
        }

        // Then
        coVerify { repository.getSymbol() }

    }
}