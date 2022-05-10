package com.codegrow.domain.usecase

import com.codegrow.domain.core.BaseUseCase
import com.codegrow.domain.core.Resource
import com.codegrow.domain.repository.SymbolRepository
import com.codegrow.domain.repository.TransactionRepository
import com.codegrow.entity.Rate
import com.codegrow.entity.Symbol
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class HistoricalInput(
    val from: String,
    val to: String,
)

class GetHistoricalSearchUseCase @Inject constructor(
    private val repository: TransactionRepository,
) : BaseUseCase<HashMap<String,Rate>, HistoricalInput>() {
    override suspend fun buildRequest(params: HistoricalInput?): Flow<Resource<HashMap<String, Rate>>> {
        return repository.getHistorical(params!!.from, params.to)
    }
}