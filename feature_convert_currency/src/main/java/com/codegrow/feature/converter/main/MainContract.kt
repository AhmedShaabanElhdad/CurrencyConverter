package com.codegrow.feature.converter.main

import com.codegrow.common.core.UiEffect
import com.codegrow.common.core.UiEvent
import com.codegrow.common.core.UiState
import com.codegrow.entity.Symbol


class MainContract {

    sealed class Event : UiEvent {
        data class Convert(
            val amount: Double? = null,
            val from: String? = null,
            val to: String? = null
        ) : Event()

        object GetSymbol: Event()
    }

    data class State(
        val mainState: MainState
    ) : UiState

    sealed class MainState {
        object Idle : MainState()
        object Loading : MainState()
        data class Success(val result : List<Symbol>) : MainState()
        data class SuccessConvert(val result : Double) : MainState()
    }

    sealed class Effect : UiEffect {
        data class ShowError(val message : String?) : Effect()
        object Navigate: Effect()
    }

}