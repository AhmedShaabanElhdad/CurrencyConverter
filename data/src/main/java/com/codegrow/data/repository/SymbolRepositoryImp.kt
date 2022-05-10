package com.codegrow.data.repository

import com.codegrow.domain.core.Resource
import com.codegrow.domain.qualifiers.IoDispatcher
import com.codegrow.domain.repository.SymbolRepository
import com.codegrow.remote.service.ApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


/**
 * Implementation class of [SymbolRepository]
 */

class SymbolRepositoryImp @Inject constructor(
    private val apiService: ApiService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : SymbolRepository {

    override suspend fun getSymbol(): Flow<Resource<HashMap<String,String>>> {
        return flow {
            try {
                val data = apiService.getSymbol("")
                if (data.success) {
                    emit(Resource.Success(data.symbols))
                } else
                    emit(Resource.Error(data.message))
            } catch (ex: Exception) {
                emit(Resource.Error(ex.message ?: "Error"))
            }
        }.flowOn(dispatcher)
    }


}