package com.codegrow.domain.usecase

import com.codegrow.domain.core.BaseUseCase
import com.codegrow.domain.core.Resource
import com.codegrow.domain.repository.SymbolRepository
import com.codegrow.entity.Symbol
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSymbolsUseCase @Inject constructor(
    private val repository: SymbolRepository,
) : BaseUseCase<HashMap<String, String>, Any>() {
    override suspend fun buildRequest(params: Any?): Flow<Resource<HashMap<String, String>>> {
        return repository.getSymbol()
    }
}