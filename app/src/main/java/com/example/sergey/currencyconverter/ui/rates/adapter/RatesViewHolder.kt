package com.example.sergey.currencyconverter.ui.rates.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sergey.currencyconverter.other.kotlinextensions.toFloatOrDefaultValue
import com.example.sergey.currencyconverter.repository.data.Rates
import com.example.sergey.currencyconverter.ui.rates.CurrenciesEnum
import kotlinx.android.synthetic.main.layout_currency_holder.view.*

class RatesViewHolder(itemView: View, private val ratesAdapterListener: RatesAdapterListener?) : RecyclerView.ViewHolder(itemView) {

    fun bind(currencyEnum: CurrenciesEnum?, rate: Float?, base: Boolean? = false) {
        if (currencyEnum == null) return
        if ((rate ?: 0f) == 0f) return

        itemView.flag.setImageDrawable(ContextCompat.getDrawable(itemView.context, currencyEnum.imageRes))
        itemView.currency_edit.setText(rate.toString())
        itemView.name_short.text = currencyEnum.name
        itemView.name.setText(currencyEnum.currencyNameRes)

        if (ratesAdapterListener == null) return
        if (base == false) {
            itemView.setOnClickListener {
                val currentValue = itemView.currency_edit.text.toFloatOrDefaultValue(Rates.DEFAULT_MULTIPLIER)
                ratesAdapterListener.onItemClick(currencyEnum, currentValue)
            }
            itemView.currency_edit.onFocusChangeListener = null
            itemView.currency_edit.keyListener = null
        } else {
            itemView.currency_edit.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    var value = Rates.DEFAULT_MULTIPLIER
                    value = s?.toFloatOrDefaultValue(value) ?: value

                    if (value == rate) return

                    ratesAdapterListener.onItemMultiplierEdit(value)
                }
            })

            itemView.currency_edit.setOnFocusChangeListener { v, hasFocus ->
                ratesAdapterListener.onItemFocusChanged(hasFocus)
            }
        }
    }
}