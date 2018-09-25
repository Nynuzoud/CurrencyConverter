package com.example.sergey.currencyconverter.ui.rates.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sergey.currencyconverter.ui.rates.CurrenciesEnum
import kotlinx.android.synthetic.main.layout_currency_holder.view.*

class RatesViewHolder(itemView: View, private val ratesAdapterListener: RatesAdapterListener?) : RecyclerView.ViewHolder(itemView) {

    fun bind(currencyEnum: CurrenciesEnum?, rate: String?) {
        if (currencyEnum == null) return
        if (rate == null) return

        itemView.flag.setImageDrawable(ContextCompat.getDrawable(itemView.context, currencyEnum.imageRes))
        itemView.name_short.text = currencyEnum.name
        itemView.name.setText(currencyEnum.currencyNameRes)

        if (adapterPosition > 0) {
            itemView.setOnClickListener {
                val currentValue = itemView.currency_edit.text.toString()
                ratesAdapterListener?.onItemClick(currencyEnum, currentValue)
            }

            itemView.currency_edit.onFocusChangeListener = null
            itemView.currency_edit.keyListener = null
            ratesAdapterListener?.getTextWatcher()?.isEnabled = false
        } else {

            itemView.currency_edit.addTextChangedListener(ratesAdapterListener?.getTextWatcher())
            ratesAdapterListener?.getTextWatcher()?.isEnabled = true
            ratesAdapterListener?.setTextWatchingView(itemView)

            itemView.currency_edit.setOnFocusChangeListener { v, hasFocus ->
                ratesAdapterListener?.onItemFocusChanged(hasFocus)
            }
        }
        itemView.currency_edit.setText(String.format("%s", rate))
    }
}