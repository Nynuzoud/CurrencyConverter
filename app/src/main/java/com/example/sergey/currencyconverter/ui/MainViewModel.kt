package com.example.sergey.currencyconverter.ui

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import com.example.sergey.currencyconverter.di.components.AppComponent
import com.example.sergey.currencyconverter.other.kotlinextensions.round
import com.example.sergey.currencyconverter.other.preferences.Preferences
import com.example.sergey.currencyconverter.repository.data.Rates
import com.example.sergey.currencyconverter.repository.interactor.rates.RatesRepository
import com.example.sergey.currencyconverter.testing.OpenForTesting
import com.example.sergey.currencyconverter.ui.rates.CurrenciesEnum
import com.example.sergey.currencyconverter.viewmodel.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@OpenForTesting
class MainViewModel @Inject constructor(applicationComponent: AppComponent,
                                        private val ratesRepository: RatesRepository,
                                        preferences: Preferences) : BaseViewModel() {


    private lateinit var ratesDisposable: Disposable

    var baseCurrency: CurrenciesEnum = CurrenciesEnum.EUR
    var currencyMultiplier = Rates.DEFAULT_MULTIPLIER
    var convertedRatesMap: LinkedHashMap<CurrenciesEnum, Float> = LinkedHashMap(33, 1f)
    val convertedRatesLiveData: MutableLiveData<LinkedHashMap<CurrenciesEnum, Float>> = MutableLiveData()

    init {
        applicationComponent.inject(this)

        //baseCurrency = preferences.getBaseCurrency()
    }

    fun startGettingRates() {
        ratesDisposable = Observable
                .interval(1, TimeUnit.SECONDS)
                .flatMap {
                    ratesRepository.getRatesRepository(baseCurrency)
                }
                .repeat()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (convertedRatesMap.isEmpty()) {
                        convertedRatesMap = generateConvertedRatesMap(it.base, it.ratesEnumMap)
                    }
                    convertedRatesLiveData.value = updateConvertedRates(it, convertedRatesMap, currencyMultiplier)
                }, {
                    Timber.d(it)
                })


        subscribe(ratesDisposable)
    }

    fun stopGettingRates() {
        unsubscribe(ratesDisposable)
    }

    fun onItemFocusChanged(hasFocus: Boolean) {
        when (hasFocus) {
            true -> stopGettingRates()
            false -> startGettingRates()
        }
    }

    fun updateBaseCurrency(currencyEnum: CurrenciesEnum, currentValue: Float) {

        moveCurrencyToTopOfMap(currencyEnum, currentValue)
        convertedRatesLiveData.value = convertedRatesMap
    }

    /**
     * Moving last accessed key to top of the map by re-creating map.
     * It can't be a LinkedHashMap with access order because it moves key to the bottom of map
     * In this case we need a map with insertion order because values update every second and reverse map will not efficient way.
     * But re-create map on user's click will be more efficient because user clicks less often
     * @param currencyEnum - [CurrenciesEnum] new base key
     * @param currentValue - [Float] old base key's value
     */
    fun moveCurrencyToTopOfMap(currencyEnum: CurrenciesEnum, currentValue: Float) {

        convertedRatesMap[baseCurrency] = currencyMultiplier
        currencyMultiplier = currentValue
        convertedRatesMap.remove(currencyEnum)

        convertedRatesMap = generateConvertedRatesMap(currencyEnum, convertedRatesMap)
    }

    /**
     * Applies new rates to all currencies
     * @param rates - [Rates] from API server
     * @param updatableMap - [LinkedHashMap] with values that should be updated
     * @param multiplier - [Float] user multiplier
     * @return updatableMap with updated values
     */
    @VisibleForTesting
    fun updateConvertedRates(rates: Rates, updatableMap: LinkedHashMap<CurrenciesEnum, Float>, multiplier: Float) : LinkedHashMap<CurrenciesEnum, Float> {

        rates.ratesEnumMap.forEach { key, value ->
            updatableMap[key] = (value * multiplier).round(2)
        }

        return updatableMap
    }

    /**
     * Generates initial [LinkedHashMap] to store [CurrenciesEnum] in default order.
     * @param base - [CurrenciesEnum] new base value
     * @param ratesMap - [MutableMap] source map
     * @return ordered by default new ratesMap
     */
    @VisibleForTesting
    fun generateConvertedRatesMap(base: CurrenciesEnum, ratesMap: MutableMap<CurrenciesEnum, Float>): LinkedHashMap<CurrenciesEnum, Float> {
        val newRatesMap = LinkedHashMap<CurrenciesEnum, Float>()
        newRatesMap[base] = currencyMultiplier

        ratesMap.forEach { key, value ->
            newRatesMap[key] = value
        }

        baseCurrency = base

        return newRatesMap
    }

    override fun onCleared() {
        super.onCleared()
        clearSubscriptions()
    }

}