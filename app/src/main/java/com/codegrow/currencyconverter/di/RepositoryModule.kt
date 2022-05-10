package com.codegrow.currencyconverter.di

import com.codegrow.data.repository.*
import com.codegrow.domain.qualifiers.IoDispatcher
import com.codegrow.domain.repository.*
import com.codegrow.remote.service.ApiService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Module that holds Repository classes
 */
@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideSymbolRepository(
        apiService: ApiService,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): SymbolRepository =
        SymbolRepositoryImp(apiService, dispatcher)

    @Provides
    @ViewModelScoped
    fun provideTransactionRepository(
        apiService: ApiService,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): TransactionRepository =
        TransactionRepositoryImp(apiService, dispatcher)



}