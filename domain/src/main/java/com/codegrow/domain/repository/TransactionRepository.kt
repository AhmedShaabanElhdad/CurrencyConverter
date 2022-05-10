package com.codegrow.domain.repository

import com.codegrow.domain.core.Resource
import com.codegrow.entity.*
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    suspend fun convert(
        amount :Double,
        from :String,
        to :String
    ): Flow<Resource<Double>>

    suspend fun getHistorical(
        start_date:String,
        end_date:String,
    ): Flow<Resource<HashMap<String,Rat>>>

}