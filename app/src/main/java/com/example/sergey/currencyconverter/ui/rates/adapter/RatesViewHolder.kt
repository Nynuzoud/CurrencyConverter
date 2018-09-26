package com.example.sergey.currencyconverter.ui.rates.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sergey.currencyconverter.ui.rates.CurrenciesEnum
import kotlinx.android.synthetic.main.layout_currency_holder.view.*

class RatesViewHolder(itemView: View, private val ratesAdapterListener: RatesAdapterListener?) : RecyclerView.ViewHolder(itemView) {

    var rate: String? = null

    fun bind(currencyEnum: CurrenciesEnum?, rate: String?) {
        if (currencyEnum == null) return
        if (rate == null) return
        this.rate = rate

        itemView.flag.setImageDrawable(ContextCompat.getDrawable(itemView.context, currencyEnum.imageRes))
        itemView.name_short.text = currencyEnum.name
        itemView.name.setText(currencyEnum.currencyNameRes)

        if (adapterPosition > 0) {
            itemView.setOnClickListener {
                ratesAdapterListener?.onItemClick(adapterPosition)
            }

            itemView.currency_edit.isFocusable = false
            itemView.currency_edit.isFocusableInTouchMode = false
            itemView.currency_edit.tag = null
            itemView.currency_edit.removeTextChangedListener(ratesAdapterListener?.getTextWatcher())

            itemView.currency_edit.setText(String.format("%s", rate))
        } else {
            itemView.currency_edit.isFocusableInTouchMode = true
            itemView.currency_edit.isFocusable = true

            if (itemView.currency_edit.tag == null) {
                itemView.currency_edit.addTextChangedListener(ratesAdapterListener?.getTextWatcher())
            }
            itemView.currency_edit.tag = "base_view"
        }
    }

    // method is needed to set up correct value after scrolling
    // and fast value replacement after configuration changed
    fun updateBaseText() {
        if (adapterPosition == 0 && rate != null) {
            itemView.currency_edit.setText(String.format("%s", rate))
            itemView.currency_edit.setSelection(itemView.currency_edit.length())
        }
    }
}