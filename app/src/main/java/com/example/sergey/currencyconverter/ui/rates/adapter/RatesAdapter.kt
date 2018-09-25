package com.example.sergey.currencyconverter.ui.rates.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sergey.currencyconverter.R
import com.example.sergey.currencyconverter.ui.rates.CurrenciesEnum
import java.util.*

class RatesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var _data: LinkedHashMap<CurrenciesEnum, String> = LinkedHashMap()
    var data: LinkedHashMap<CurrenciesEnum, String>?
        get() = _data
        set(value) {
            if (value != null) _data = value
            notifyDataSetChanged()
        }

    var listener: RatesAdapterListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RatesViewHolder(
                LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.layout_currency_holder, parent, false),
                listener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RatesViewHolder).bind(_data.keys.elementAt(position), _data.values.elementAt(position))
    }

    override fun getItemCount(): Int = _data.size

    override fun getItemId(position: Int): Long = _data.keys.elementAt(position).ordinal.toLong()
}