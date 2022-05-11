package com.codegrow.feature.converter.details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codegrow.entity.Rate
import com.codegrow.feature.databinding.ItemHistoricalBinding

/**
 * Adapter class for RecyclerView
 */
class HistoricalAdapter constructor(
    private val result: List<Rate>,
    private val clickFunc : ((Rate) -> Unit)? = null
) : RecyclerView.Adapter<HistoricalViewHolder>() {

    val data:MutableList<Rate> = result.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricalViewHolder {
        val binding = ItemHistoricalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )


        return HistoricalViewHolder(binding = binding, click = clickFunc)

    }

    fun appendList(list:List<Rate>){
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: HistoricalViewHolder, position: Int) {
        holder.doBindings((data[position]))
        holder.bind()
    }

    override fun getItemCount(): Int {
        return data.size
    }


}
