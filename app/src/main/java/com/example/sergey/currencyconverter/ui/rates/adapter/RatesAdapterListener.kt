package com.example.sergey.currencyconverter.ui.rates.adapter

interface RatesAdapterListener {

    fun onItemClick(adapterPosition: Int)

    fun onItemMultiplierEdit(multiplier: String)

    fun afterTextChanged()

    fun onItemFocusChanged(hasFocus: Boolean)

    fun getTextWatcher(): BaseRateTextWatcher?
}