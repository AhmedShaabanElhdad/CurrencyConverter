package com.codegrow.feature.converter.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.codegrow.common.core.BaseFragment
import com.codegrow.feature.converter.details.adapter.HistoricalAdapter
import com.codegrow.feature.converter.main.adapter.SymbolSpinnerAdapter
import com.codegrow.feature.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.*

@AndroidEntryPoint
class DetailsFragment : BaseFragment<FragmentDetailBinding>() {


    private val viewModel: DetailsViewModel by viewModels()

    private val adapter: HistoricalAdapter by lazy {
        HistoricalAdapter(emptyList()){

        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calender = Calendar.getInstance()
        var month = if((calender.get(Calendar.MONTH )-1)<10) "0${calender.get(Calendar.MONTH)-1}" else calender.get(Calendar.MONTH)-1
        var day = if(calender.get(Calendar.DAY_OF_MONTH)<10) "0${calender.get(Calendar.DAY_OF_MONTH)}" else calender.get(Calendar.DAY_OF_MONTH )

        val to = "${calender.get(Calendar.YEAR)}-${month}-${day}"

        calender.add(Calendar.DAY_OF_MONTH,-2)
        month = if((calender.get(Calendar.MONTH )-1)<10) "0${calender.get(Calendar.MONTH)-1}" else calender.get(Calendar.MONTH) -1
        day = if(calender.get(Calendar.DAY_OF_MONTH)<10) "0${calender.get(Calendar.DAY_OF_MONTH)}" else calender.get(Calendar.DAY_OF_MONTH )

        val from = "${calender.get(Calendar.YEAR)}-${month}-${day}"
        viewModel.setEvent(DetailsContract.Event.GetHistorical(from,to))

        if (binding.rvRate.adapter == null)
            binding.rvRate.adapter = adapter
    }

    override val bindLayout: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDetailBinding
        get() = FragmentDetailBinding::inflate

    override fun prepareView(savedInstanceState: Bundle?) {

        initObservers()

    }


    /**
     * Initialize Observers
     */
    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                when (val state = it.mainState) {
                    is DetailsContract.DetailsState.Idle -> {
//                        binding.loadingPb.isVisible = false
                    }
                    is DetailsContract.DetailsState.Loading -> {
//                        binding.loadingPb.isVisible = true
                    }
                    is DetailsContract.DetailsState.Success -> {
                        val data = state.result
                        adapter.appendList(data)
//                        binding.loadingPb.isVisible = false
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.effect.collect {
                when (it) {
                    is DetailsContract.Effect.ShowError -> {
                        val msg = it.message
                        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}