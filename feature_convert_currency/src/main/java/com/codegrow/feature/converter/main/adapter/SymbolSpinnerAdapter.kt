package com.codegrow.feature.converter.main.adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView

import android.view.ViewGroup

import android.widget.ArrayAdapter
import com.codegrow.entity.Symbol


class SymbolSpinnerAdapter(
    context: Context, textViewResourceId: Int,
    values: List<Symbol>
) : ArrayAdapter<Symbol>(context, textViewResourceId, values) {

    private var values: List<Symbol> = values

    override fun getCount(): Int {
        return values.size
    }

    override fun getItem(position: Int): Symbol {
        return values[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        val label = super.getView(position, convertView, parent!!) as TextView
        label.setTextColor(Color.BLACK)
        label.text = values[position].symbol

        // And finally return your dynamic (or custom) view for each spinner item
        return label
    }

    override fun getDropDownView(
        position: Int, convertView: View?,
        parent: ViewGroup?
    ): View {
        val label = super.getDropDownView(position, convertView, parent) as TextView
        label.setTextColor(Color.BLACK)
        label.text = values[position].symbol
        return label
    }

    fun appendList(data: List<Symbol>) {
        this.values =data
        notifyDataSetChanged()
    }

}