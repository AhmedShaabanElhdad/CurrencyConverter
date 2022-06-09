package com.codegrow.feature.converter.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.codegrow.common.core.BaseViewModel
import com.codegrow.domain.core.Resource
import com.codegrow.domain.usecase.ConvertInput
import com.codegrow.domain.usecase.ConvertUseCase
import com.codegrow.domain.usecase.GetSymbolsUseCase
import com.codegrow.entity.Symbol
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.HashMap
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val convertUseCase: ConvertUseCase,
    private val getSymbolsUseCase: GetSymbolsUseCase,
) : BaseViewModel<MainContract.Event, MainContract.State, MainContract.Effect>() {


    private var _loading: Boolean = false
    val loading: Boolean get() = _loading

    override fun createInitialState(): MainContract.State {
        return MainContract.State(
            mainState = MainContract.MainState.Idle
        )
    }

    override fun handleEvent(event: MainContract.Event) {
        when (event) {
            is MainContract.Event.GetSymbol -> {
                getSymbol()
            }
            is MainContract.Event.Convert -> {
                convert(event.amount,event.from,event.to)
            }
        }
    }

    /**
     * convert
     */
    private fun convert(amount: Double?, from: String?, to: String?) {
        _loading = true
        viewModelScope.launch {
            convertUseCase.execute(
                ConvertInput(
                    from = from!!,
                    to = to!!,
                    amount = amount
                )
            ).onStart { emit(Resource.Loading) }
                .collect {
                    _loading = false
                    when (it) {
                        is Resource.Loading -> {
                            // Set State
                            setState { copy(mainState = MainContract.MainState.Loading) }
                        }
                        is Resource.Success -> {
                            setState {
                                copy(
                                    mainState = MainContract.MainState.SuccessConvert(
                                        result = it.data
                                    )
                                )
                            }
                        }
                        is Resource.Error -> {
                            // Set Effect
                            setEffect { MainContract.Effect.ShowError(message = it.message) }
                        }
                    }
                }
        }
    }



    /**
     * get Symbol
     */
    private fun getSymbol() {
        _loading = true
        viewModelScope.launch {
            getSymbolsUseCase.execute(Any()).onStart { emit(Resource.Loading) }
                .collect {
                    _loading = false
                    when (it) {
                        is Resource.Loading -> {
                            // Set State
                            setState { copy(mainState = MainContract.MainState.Loading) }
                        }
                        is Resource.Success -> {

                            setState {
                                copy(
                                    mainState = MainContract.MainState.Success(
                                        result = mapToArray(it.data)
                                    )
                                )
                            }
                        }
                        is Resource.Error -> {
                            // Set Effect
                            setEffect { MainContract.Effect.ShowError(message = it.message) }
                        }
                    }
                }
        }
    }

    private fun mapToArray(data: HashMap<String, String>): List<Symbol> {
        val symbols:MutableList<Symbol> = emptyList<Symbol>().toMutableList()
        for ((key, value) in data) {
            symbols.add(Symbol(key,value))
        }
        return  symbols
    }
}