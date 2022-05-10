package com.codegrow.data.repository

import com.codegrow.domain.core.Resource
import com.codegrow.domain.qualifiers.IoDispatcher
import com.codegrow.domain.repository.TransactionRepository
import com.codegrow.entity.Rate
import com.codegrow.remote.service.ApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


/**
 * Implementation class of [TransactionRepository]
 */

class TransactionRepositoryImp @Inject constructor(
    private val apiService: ApiService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : TransactionRepository {


    override suspend fun convert(amount: Double, from: String, to: String): Flow<Resource<Double>> {
        return flow {
            try {
                val data = apiService.convert(amount,from,to,"")
                if (data.success) {
                    emit(Resource.Success(data.result))
                } else
                    emit(Resource.Error(data.message))
            } catch (ex: Exception) {
                emit(Resource.Error(ex.message ?: "Error"))
            }
        }.flowOn(dispatcher)
    }

    override suspend fun getHistorical(
        start_date:String,
        end_date:String,
    ): Flow<Resource<HashMap<String,Rate>>> {
        return flow {
            try {
                val data = apiService.getHistoricalSearch(start_date,end_date,"")
                if (data.success) {
                    emit(Resource.Success(data.rates))
                } else
                    emit(Resource.Error(data.message))
            } catch (ex: Exception) {
                emit(Resource.Error(ex.message ?: "Error"))
            }
        }.flowOn(dispatcher)
    }



}