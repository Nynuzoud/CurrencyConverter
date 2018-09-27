package com.example.sergey.currencyconverter.ui

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import com.example.sergey.currencyconverter.R
import com.example.sergey.currencyconverter.di.components.AppComponent
import com.example.sergey.currencyconverter.repository.data.Rates
import com.example.sergey.currencyconverter.repository.interactor.rates.RatesRepository
import com.example.sergey.currencyconverter.testing.OpenForTesting
import com.example.sergey.currencyconverter.ui.rates.CurrenciesEnum
import com.example.sergey.currencyconverter.ui.rates.adapter.BaseRateTextWatcher
import com.example.sergey.currencyconverter.viewmodel.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.IOException
import java.math.BigDecimal
import java.net.UnknownHostException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@OpenForTesting
class MainViewModel @Inject constructor(applicationComponent: AppComponent,
                                        private val ratesRepository: RatesRepository) : BaseViewModel() {

    private var ratesDisposable: Disposable? = null

    val errorsLiveData = MutableLiveData<Int>()

    var baseCurrency: CurrenciesEnum = CurrenciesEnum.EUR
    private var _currencyMultiplier: String = Rates.DEFAULT_MULTIPLIER
    var currencyMultiplier: String
        get() = _currencyMultiplier
        set(value) {
            _currencyMultiplier = value
            if (baseCurrency != rates?.base) {
                rates = generatePredictedRates()
            }
            convertedRatesLiveData.value = updateConvertedRates(rates
                    ?: return, convertedRatesLiveData.value ?: return, value)
        }

    @VisibleForTesting
    var rates: Rates? = null
    val convertedRatesLiveData = MutableLiveData<LinkedHashMap<CurrenciesEnum, String>>()
    var baseTextWatcher: BaseRateTextWatcher = BaseRateTextWatcher()

    init {
        applicationComponent.inject(this)
    }

    fun startGettingRates() {
        unsubscribe(ratesDisposable)
        ratesDisposable = Observable
                .interval(1, TimeUnit.SECONDS)
                .flatMap {
                    ratesRepository.getRatesRepository(baseCurrency)
                }
                .repeat()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (convertedRatesLiveData.value == null || convertedRatesLiveData.value?.isEmpty() == true) {
                        convertedRatesLiveData.value = generateConvertedRatesMap(it.base, it.ratesEnumMap)
                    }
                    val map = convertedRatesLiveData.value
                    if (map != null) {
                        convertedRatesLiveData.value = updateConvertedRates(it, map, _currencyMultiplier)
                    }
                    rates = it
                }, {
                    when (it) {
                        is UnknownHostException -> errorsLiveData.value = R.string.network_error
                        is IOException -> {} //do nothing
                        else -> errorsLiveData.value = R.string.unknown_error
                    }
                    Timber.d(it)
                })


        subscribe(ratesDisposable)
    }

    fun stopGettingRates() {
        unsubscribe(ratesDisposable)
    }

    /**
     * Generates expected rates to make a fast data replacement after user's changes
     * This rates will be updated with actual data after server response
     * @return [Rates]
     */
    @VisibleForTesting
    fun generatePredictedRates(): Rates {
        val ratesEnumMap = EnumMap<CurrenciesEnum, String>(CurrenciesEnum::class.java)
        val newCurrencyRate = rates?.ratesEnumMap?.get(baseCurrency) ?: "1"
        CurrenciesEnum.values().forEach {
            if (it != baseCurrency) {
                val oldRateValue = rates?.ratesEnumMap?.get(it) ?: "1"

                ratesEnumMap[it] = BigDecimal(oldRateValue)
                        .divide(BigDecimal(newCurrencyRate),
                                Rates.DEFAULT_ROUND_SCALE,
                                Rates.DEFAULT_ROUNDING)
                        .toString()
            }
        }

        return Rates(
                base = baseCurrency,
                ratesEnumMap = ratesEnumMap
        )
    }

    fun updateBaseCurrency(adapterPosition: Int) {
        if (adapterPosition < 0) return

        val currencyEnum = convertedRatesLiveData.value?.keys?.elementAt(adapterPosition)
        val currentValue = convertedRatesLiveData.value?.get(currencyEnum)

        moveCurrencyToTopOfMap(currencyEnum ?: return, currentValue ?: return)
    }

    /**
     * Moving last accessed key to top of the map by re-creating map.
     * It can't be a LinkedHashMap with access order because it moves key to the bottom of map
     * In this case we need a map with insertion order because values update every second and reverse map will not efficient way.
     * But re-create map on user's click will be more efficient because user clicks less often
     * @param currencyEnum - [CurrenciesEnum] new base key
     * @param currentValue - [Float] old base key's value
     */
    fun moveCurrencyToTopOfMap(currencyEnum: CurrenciesEnum, currentValue: String) {

        convertedRatesLiveData.value = generateConvertedRatesMap(currencyEnum, convertedRatesLiveData.value
                ?: return)
        _currencyMultiplier = currentValue
    }

    /**
     * Applies new rates to all currencies
     * @param rates - [Rates] from API server
     * @param updatableMap - [LinkedHashMap] with values that should be updated
     * @param multiplier - [Float] user multiplier
     * @return updatableMap with updated values
     */
    @VisibleForTesting
    fun updateConvertedRates(rates: Rates, updatableMap: LinkedHashMap<CurrenciesEnum, String>, multiplier: String): LinkedHashMap<CurrenciesEnum, String> {

        updatableMap[baseCurrency] = multiplier

        rates.ratesEnumMap.forEach { key, value ->
            updatableMap[key] = BigDecimal(value).multiply(BigDecimal(multiplier)).setScale(Rates.DEFAULT_ROUND_SCALE, Rates.DEFAULT_ROUNDING).toString()
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
    fun generateConvertedRatesMap(base: CurrenciesEnum, ratesMap: MutableMap<CurrenciesEnum, String>): LinkedHashMap<CurrenciesEnum, String> {
        val newRatesMap = LinkedHashMap<CurrenciesEnum, String>(CurrenciesEnum.values().size, 1f)
        newRatesMap[base] = ratesMap[base] ?: _currencyMultiplier

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