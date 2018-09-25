package com.example.sergey.currencyconverter.ui.rates.adapter

import android.view.View
import com.example.sergey.currencyconverter.ui.rates.CurrenciesEnum

interface RatesAdapterListener {

    fun onItemClick(currencyEnum: CurrenciesEnum, currentValue: String)

    fun onItemMultiplierEdit(multiplier: String)

    fun onItemFocusChanged(hasFocus: Boolean)

    fun getTextWatcher(): BaseRateTextWatcher?

    fun setTextWatchingView(view: View)
}