package com.codegrow.domain.repository

import com.codegrow.domain.core.Resource
import com.codegrow.entity.*
import kotlinx.coroutines.flow.Flow

interface SymbolRepository {

    suspend fun getSymbol(): Flow<Resource<List<Symbol>>>

}