package com.codegrow.feature.converter.details

import com.codegrow.common.core.UiEffect
import com.codegrow.common.core.UiEvent
import com.codegrow.common.core.UiState
import com.codegrow.entity.Rate


class DetailsContract {

    sealed class Event : UiEvent {
        data class GetHistorical(val from: String, val to: String) : Event()
    }

    data class State(
        val mainState: DetailsState
    ) : UiState

    sealed class DetailsState {
        object Idle : DetailsState()
        object Loading : DetailsState()
        data class Success(val result: List<Rate>) : DetailsState()
    }

    sealed class Effect : UiEffect {
        data class ShowError(val message: String?) : Effect()
    }

}