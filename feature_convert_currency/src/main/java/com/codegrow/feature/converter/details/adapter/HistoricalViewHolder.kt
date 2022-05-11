package com.codegrow.feature.converter.details.adapter

import com.codegrow.entity.Rate
import com.codegrow.feature.converter.core.BaseViewHolder
import com.codegrow.feature.databinding.ItemHistoricalBinding


/**
 * ViewHolder class for Historical
 */
class HistoricalViewHolder constructor(
    private val binding : ItemHistoricalBinding,
    private val click : ((Rate) -> Unit)? = null
) : BaseViewHolder<Rate, ItemHistoricalBinding>(binding) {


    init {
        binding.root.setOnClickListener {
            click?.invoke(getRowItem()!!)
        }
    }

    override fun bind() {


        getRowItem()?.let {

            binding.rate = it
            binding.executePendingBindings()
        }
    }
}