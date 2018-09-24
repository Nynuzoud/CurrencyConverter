package com.example.sergey.currencyconverter.ui.rates.adapter

import com.example.sergey.currencyconverter.ui.rates.CurrenciesEnum

interface RatesAdapterListener {

    fun onItemClick(currencyEnum: CurrenciesEnum, currentValue: Float)

    fun onItemMultiplierEdit(multiplier: Float)

    fun onItemFocusChanged(hasFocus: Boolean)
}