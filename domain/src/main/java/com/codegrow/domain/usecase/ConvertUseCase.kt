package com.codegrow.domain.usecase

import com.codegrow.domain.R
import com.codegrow.domain.core.BaseUseCase
import com.codegrow.domain.core.Resource
import com.codegrow.domain.repository.SymbolRepository
import com.codegrow.domain.repository.TransactionRepository
import com.codegrow.entity.Symbol
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.temporal.TemporalAmount
import javax.inject.Inject

data class ConvertInput(
    val amount: Double?,
    val from: String,
    val to: String,
)

class ConvertUseCase @Inject constructor(
    private val repository: TransactionRepository,
) : BaseUseCase<Double, ConvertInput>() {
    override suspend fun buildRequest(params: ConvertInput?): Flow<Resource<Double>> {
        if (params?.amount == null || params.amount.isNaN()) {
            return flow { emit(Resource.Error("invalid Amount")) }
        }
        if (params.from.isEmpty()) {
            return flow { emit(Resource.Error("invalid symbol from")) }
        }
        if (params.to.isEmpty()) {
            return flow { emit(Resource.Error("invalid symbol to")) }
        }
        return repository.convert(amount = params!!.amount!!, from = params.from, to = params.to)
    }
}