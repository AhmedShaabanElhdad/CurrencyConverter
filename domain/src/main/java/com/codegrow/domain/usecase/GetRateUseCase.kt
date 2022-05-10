package com.codegrow.domain.usecase

import com.codegrow.domain.core.BaseUseCase
import com.codegrow.domain.core.Resource
import com.codegrow.domain.repository.SymbolRepository
import com.codegrow.entity.Symbol
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRateUseCase @Inject constructor(
    private val repository: SymbolRepository,
) : BaseUseCase<List<Symbol>, Any>() {
    override suspend fun buildRequest(params: Any?): Flow<Resource<List<Symbol>>> {
        return repository.getSymbol()
    }
}