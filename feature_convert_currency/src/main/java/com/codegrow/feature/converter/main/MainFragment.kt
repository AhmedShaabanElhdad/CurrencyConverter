package com.codegrow.feature.converter.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.codegrow.common.core.BaseFragment
import com.codegrow.entity.Symbol
import com.codegrow.feature.R
import com.codegrow.feature.converter.main.adapter.SymbolSpinnerAdapter
import com.codegrow.feature.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>() {


    private val viewModel: MainViewModel by viewModels()

    private val fromAdapter: SymbolSpinnerAdapter by lazy {
        SymbolSpinnerAdapter(requireContext(), android.R.layout.simple_spinner_item, emptyList())
    }
    private val toAdapter: SymbolSpinnerAdapter by lazy {
        SymbolSpinnerAdapter(requireContext(), android.R.layout.simple_spinner_item, emptyList())
    }


    override val bindLayout: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMainBinding
        get() = FragmentMainBinding::inflate

    override fun prepareView(savedInstanceState: Bundle?) {
        setupSpinner()
        setEventListener()
        initObservers()

    }

    private fun setupSpinner() {
        binding.spnFirstCountry.adapter = fromAdapter
        binding.spnSecondCountry.adapter = toAdapter
    }

    private fun setEventListener() {
        binding.btnConvert.setOnClickListener {
            viewModel.setEvent(
                MainContract.Event.Convert(
                    binding.etFirstCurrency.text.toString().toDouble(),
                    (binding.spnFirstCountry.selectedItem as Symbol).symbol,
                    (binding.spnSecondCountry.selectedItem as Symbol).symbol
                )
            )
        }

        binding.btnDetails.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_detailFragment)
        }
    }


    /**
     * Initialize Observers
     */
    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                when (val state = it.mainState) {
                    is MainContract.MainState.Idle -> {
                        binding.spinKit.isVisible = false
                    }
                    is MainContract.MainState.Loading -> {
                        binding.spinKit.isVisible = true
                    }
                    is MainContract.MainState.Success -> {
                        val data = state.result
                        fromAdapter.appendList(data)
                        toAdapter.appendList(data)
                        binding.spinKit.isVisible = false
                    }
                    is MainContract.MainState.SuccessConvert -> {
                        binding.btnConvert.visibility = View.VISIBLE
                        binding.etSecondCurrency.setText(state.result.toString())
                        binding.spinKit.isVisible = false
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.effect.collect {
                when (it) {
                    is MainContract.Effect.ShowError -> {
                        val msg = it.message
                        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                    }
                    is MainContract.Effect.Navigate -> {

                    }
                }
            }
        }
    }
}