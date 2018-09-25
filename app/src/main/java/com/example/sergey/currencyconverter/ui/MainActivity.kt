package com.example.sergey.currencyconverter.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.sergey.currencyconverter.R
import com.example.sergey.currencyconverter.di.ComponentsHolder
import com.example.sergey.currencyconverter.ui.rates.adapter.BaseRateTextWatcher
import com.example.sergey.currencyconverter.ui.rates.adapter.RatesAdapter
import com.example.sergey.currencyconverter.ui.rates.adapter.RatesAdapterListener
import com.example.sergey.currencyconverter.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: MainViewModel

    private val ratesAdapter = RatesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ComponentsHolder.applicationComponent.inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        loading.visibility = View.VISIBLE

        ratesAdapter.listener = RatesAdapterListenerImpl()
        viewModel.baseTextWatcher.ratesAdapterListener = ratesAdapter.listener
        ratesAdapter.setHasStableIds(true)
        rates_recycler.adapter = ratesAdapter
        rates_recycler.setHasFixedSize(true)

        viewModel.convertedRatesLiveData.observe(this, Observer {
            //simple check to avoid unexpected crashes
            if (!rates_recycler.isComputingLayout) {
                ratesAdapter.data = it
                loading.visibility = View.GONE
            }
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.startGettingRates()
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopGettingRates()
    }

    private inner class RatesAdapterListenerImpl : RatesAdapterListener {

        override fun onItemClick(adapterPosition: Int) {
            viewModel.stopGettingRates()
            viewModel.updateBaseCurrency(adapterPosition)
            viewModel.startGettingRates()
        }

        override fun onItemMultiplierEdit(multiplier: String) {
            viewModel.currencyMultiplier = multiplier
        }

        override fun onItemFocusChanged(hasFocus: Boolean) {
            viewModel.onItemFocusChanged(hasFocus)
        }

        override fun afterTextChanged() {
            viewModel.startGettingRates()
        }

        override fun getTextWatcher(): BaseRateTextWatcher? = viewModel.baseTextWatcher
    }
}
