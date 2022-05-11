package com.codegrow.feature.converter.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.codegrow.common.core.BaseViewModel
import com.codegrow.domain.core.Resource
import com.codegrow.domain.usecase.*
import com.codegrow.entity.Rate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.HashMap
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getHistoricalSearchUseCase: GetHistoricalSearchUseCase,
) : BaseViewModel<DetailsContract.Event, DetailsContract.State, DetailsContract.Effect>() {


    private var _loading: Boolean = false
    val loading: Boolean get() = _loading


    override fun createInitialState(): DetailsContract.State {
        return DetailsContract.State(
            mainState = DetailsContract.DetailsState.Idle
        )
    }

    override fun handleEvent(event: DetailsContract.Event) {
        when (event) {
            is DetailsContract.Event.GetHistorical -> {
                getHistorical(event.from, event.to)
            }
        }
    }

    /**
     * Fetch posts
     */
    private fun getHistorical(from: String?, to: String?) {

        _loading = true

        viewModelScope.launch {
            getHistoricalSearchUseCase.execute(
                HistoricalInput(
                    from = from!!,
                    to = to!!,
                )
            ).onStart { emit(Resource.Loading) }
                .collect {
                    _loading = false
                    when (it) {
                        is Resource.Loading -> {
                            // Set State
                            setState { copy(mainState = DetailsContract.DetailsState.Loading) }
                        }
                        is Resource.Success -> {
                            setState {
                                copy(
                                    mainState = DetailsContract.DetailsState.Success(
                                        result =  mapToArray(it.data)
                                    )
                                )
                            }
                        }
                        is Resource.Error -> {
                            // Set Effect
                            setEffect { DetailsContract.Effect.ShowError(message = it.message) }
                        }
                    }
                }
        }
    }


    private fun mapToArray(data: HashMap<String, Rate>): List<Rate> {
        var rates:MutableList<Rate> = emptyList<Rate>().toMutableList()
        data.forEach{ (key, value) ->
            rates.add(Rate(
                date = key,
                CAD = value.CAD,
                EUR = value.EUR,
                USD = value.USD
            ))
        }
        return  rates
    }
}