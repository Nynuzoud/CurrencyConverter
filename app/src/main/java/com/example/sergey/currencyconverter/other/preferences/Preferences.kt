package com.example.sergey.currencyconverter.other.preferences

import com.example.sergey.currencyconverter.ui.rates.CurrenciesEnum

interface Preferences {

    fun setBaseCurrency(currenciesEnum: CurrenciesEnum)
    fun getBaseCurrency(): CurrenciesEnum
}