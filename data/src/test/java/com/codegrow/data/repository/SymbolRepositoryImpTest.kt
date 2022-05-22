package com.codegrow.data.repository

import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.codegrow.data.core.TestUtilities
import com.codegrow.data.core.MainCoroutineRule
import com.codegrow.domain.core.Resource
import com.codegrow.domain.repository.SymbolRepository
import com.codegrow.remote.service.ApiService
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.ExperimentalTime



@ExperimentalTime
@ExperimentalCoroutinesApi
@SmallTest
class SymbolRepositoryImpTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var apiService: ApiService

    private lateinit var repository: SymbolRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks
        // Create RepositoryImp before every test
        repository = SymbolRepositoryImp(
            apiService = apiService,
            dispatcher = mainCoroutineRule.dispatcher
        )
    }

    @Test
    fun test_get_symbols_remote_success() = runBlockingTest {

        val symbolResponse = TestUtilities.getSymbolsTestObject()

        // Given
        coEvery { apiService.getSymbol(any()) } returns symbolResponse


        // When & Assertions
        val flow = repository.getSymbol()
        flow.test {
            // Expect Resource.Success
            val expected = expectItem()
            val expectedData = (expected as Resource.Success).data
            Truth.assertThat(expected).isInstanceOf(Resource.Success::class.java)
            Truth.assertThat(expectedData.size).isEqualTo(symbolResponse.symbols.size)
            expectComplete()
        }

        // Then
        coVerify { apiService.getSymbol(any()) }
    }

    @Test
    fun test_get_symbols_remote_fail() = runBlockingTest {


        val symbolResponse = TestUtilities.getSymbolsTestObject()

        // Given
        coEvery { apiService.getSymbol(any()) } throws Exception()


        // When & Assertions
        val flow = repository.getSymbol()
        flow.test {
            // Expect Resource.Success
            Truth.assertThat(expectItem()).isInstanceOf(Resource.Error::class.java)
            expectComplete()
        }
        // Then
        coVerify { apiService.getSymbol(any()) }

    }
}