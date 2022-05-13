package com.codegrow.feature.converter.details

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.codegrow.common.core.BaseFragment
import com.codegrow.entity.Rate
import com.codegrow.feature.converter.details.adapter.HistoricalAdapter
import com.codegrow.feature.converter.main.adapter.SymbolSpinnerAdapter
import com.codegrow.feature.databinding.FragmentDetailBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
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
                        SetupGroupBarChart(data)
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





    fun SetupGroupBarChart(rates:List<Rate>) {

        binding.mChart.setDrawBarShadow(false)
        binding.mChart.description.isEnabled = false
        binding.mChart.setPinchZoom(false)
        binding.mChart.setDrawGridBackground(true)

        // empty labels so that the names are spread evenly
        val labels = rates.map { it.date }.toMutableList()
        labels.add(0,"")
        labels.add("")
        labels.add("")

        val xAxis: XAxis = binding.mChart.xAxis
        xAxis.setCenterAxisLabels(true)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(true)
        xAxis.granularity = 1f // only intervals of 1 day
        xAxis.textColor = Color.BLACK
        xAxis.textSize = 12f
        xAxis.axisLineColor = Color.WHITE
        xAxis.axisMinimum = 1f
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)


        val leftAxis: YAxis = binding.mChart.axisLeft
        leftAxis.textColor = Color.BLACK
        leftAxis.textSize = 12f
        leftAxis.axisLineColor = Color.WHITE
        leftAxis.setDrawGridLines(true)
        leftAxis.granularity = 2f
        leftAxis.setLabelCount(8, true)
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        binding.mChart.axisRight.isEnabled = false
        binding.mChart.legend.isEnabled = false


        val valOne = floatArrayOf(rates[0].CAD.toFloat(), rates[0].EUR.toFloat(),rates[0].USD.toFloat())
        val valTwo = floatArrayOf(rates[1].CAD.toFloat(), rates[1].EUR.toFloat(),rates[1].USD.toFloat())
        val valThree = floatArrayOf(rates[2].CAD.toFloat(), rates[2].EUR.toFloat(),rates[2].USD.toFloat())

        val barOne: ArrayList<BarEntry> = ArrayList()
        val barTwo: ArrayList<BarEntry> = ArrayList()
        val barThree: ArrayList<BarEntry> = ArrayList()

        for (i in valOne.indices) {
            barOne.add(BarEntry(i.toFloat(), valOne[i]))
            barTwo.add(BarEntry(i.toFloat(), valTwo[i]))
            barThree.add(BarEntry(i.toFloat(), valThree[i]))
        }

        val set1 = BarDataSet(barOne, "barOne")
        set1.color = Color.BLUE
        val set2 = BarDataSet(barTwo, "barTwo")
        set2.color = Color.MAGENTA
        val set3 = BarDataSet(barThree, "barTwo")
        set2.color = Color.GREEN

        set1.isHighlightEnabled = false
        set2.isHighlightEnabled = false
        set3.isHighlightEnabled = false
        set1.setDrawValues(false)
        set2.setDrawValues(false)
        set3.setDrawValues(false)


        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(set1)
        dataSets.add(set2)
        dataSets.add(set3)
        val data = BarData(dataSets)
        val groupSpace = 0.4f
        val barSpace = 0f
        val barWidth = 0.3f

        data.barWidth = barWidth
        xAxis.axisMaximum = labels.size - 1.1f

        binding.mChart.data = data
        binding.mChart.setScaleEnabled(false)
        binding.mChart.setVisibleXRangeMaximum(6f)
        binding.mChart.groupBars(1f, groupSpace, barSpace)
        binding.mChart.invalidate()
    }

}