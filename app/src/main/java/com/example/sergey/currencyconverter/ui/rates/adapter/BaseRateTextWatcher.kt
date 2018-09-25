package com.example.sergey.currencyconverter.ui.rates.adapter

import android.text.Editable
import android.text.TextWatcher

/**
 * This class is needed to store and handle only one object of [BaseRateTextWatcher] to avoid different problems
 * The main problem is that any [TextWatcher] still working some time after calling [android.widget.TextView.removeTextChangedListener]
 * Solution is to add [isEnabled] property to check whether [TextWatcher] is enabled or not
 */
class BaseRateTextWatcher : TextWatcher {

    var ratesAdapterListener: RatesAdapterListener? = null

    var isEnabled = false

    private var oldValue: String = ""

    override fun afterTextChanged(s: Editable?) {
        ratesAdapterListener?.afterTextChanged()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (!isEnabled) return

        var value = s?.removePrefix("0").toString()

        if (value == oldValue) return
        oldValue = value

        if (value.isEmpty()) value = "0"
        ratesAdapterListener?.onItemMultiplierEdit(value)
    }
}