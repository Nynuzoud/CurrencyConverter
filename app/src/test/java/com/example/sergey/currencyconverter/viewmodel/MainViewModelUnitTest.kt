package com.example.sergey.currencyconverter.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.sergey.currencyconverter.di.components.AppComponent
import com.example.sergey.currencyconverter.repository.data.Rates
import com.example.sergey.currencyconverter.repository.interactor.rates.RatesRepository
import com.example.sergey.currencyconverter.ui.MainViewModel
import com.example.sergey.currencyconverter.ui.rates.CurrenciesEnum
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import java.math.BigDecimal
import java.util.*


@RunWith(JUnit4::class)
class MainViewModelUnitTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var rates: Rates

    private val AUD_VAL = "1.61"
    private val BGN_VAL = "1.95"
    private val BRL_VAL = "1.79"

    private val appComponent = mock(AppComponent::class.java)
    private val ratesRepository = mock(RatesRepository::class.java)
    private var viewModel = MainViewModel(appComponent, ratesRepository)

    @Before
    fun init() {

        val enumMap = EnumMap<CurrenciesEnum, String>(CurrenciesEnum::class.java)
        enumMap[CurrenciesEnum.AUD] = AUD_VAL
        enumMap[CurrenciesEnum.BGN] = BGN_VAL
        enumMap[CurrenciesEnum.BRL] = BRL_VAL

        rates = Rates(
                base = CurrenciesEnum.EUR,
                ratesEnumMap = enumMap
        )
    }

    @Test
    fun checkGenerateConvertedRatesWorksSuccessfully() {
        val resultMap = viewModel.generateConvertedRatesMap(rates.base, rates.ratesEnumMap)

        assert(resultMap.isNotEmpty())
        assert(resultMap.keys.indexOf(CurrenciesEnum.EUR) == 0)
        assert(resultMap.keys.indexOf(CurrenciesEnum.AUD) == 1)
        assert(resultMap.keys.indexOf(CurrenciesEnum.BGN) == 2)
        assert(resultMap.keys.indexOf(CurrenciesEnum.BRL) == 3)

        assert(resultMap[CurrenciesEnum.AUD] == AUD_VAL)
        assert(resultMap[CurrenciesEnum.BGN] == BGN_VAL)
        assert(resultMap[CurrenciesEnum.BRL] == BRL_VAL)
    }

    @Test
    fun checkUpdateConvertedRatesReturnsUpdatedValues() {

        var multiplier = "1"
        viewModel.convertedRatesLiveData.value = viewModel.generateConvertedRatesMap(rates.base, rates.ratesEnumMap)

        val resultMap = viewModel.updateConvertedRates(rates, viewModel.convertedRatesLiveData.value!!, multiplier)

        assert(resultMap[CurrenciesEnum.AUD] == AUD_VAL)
        assert(resultMap[CurrenciesEnum.BGN] == BGN_VAL)
        assert(resultMap[CurrenciesEnum.BRL] == BRL_VAL)

        val audVal2 = "2.48"
        val bgnVal2 = "2.81"
        val brlVal2 = "2.13"

        val enumMap = EnumMap<CurrenciesEnum, String>(CurrenciesEnum::class.java)
        enumMap[CurrenciesEnum.AUD] = audVal2
        enumMap[CurrenciesEnum.BGN] = bgnVal2
        enumMap[CurrenciesEnum.BRL] = brlVal2

        val rates2 = Rates(
                base = CurrenciesEnum.EUR,
                ratesEnumMap = enumMap
        )

        val resultMap2 = viewModel.updateConvertedRates(rates2, viewModel.convertedRatesLiveData.value!!, multiplier)

        assert(resultMap2[CurrenciesEnum.AUD] == audVal2)
        assert(resultMap2[CurrenciesEnum.BGN] == bgnVal2)
        assert(resultMap2[CurrenciesEnum.BRL] == brlVal2)

        multiplier = "4.34"

        val resultMap3 = viewModel.updateConvertedRates(rates2, viewModel.convertedRatesLiveData.value!!, multiplier)

        val audResult = BigDecimal(audVal2).multiply(BigDecimal(multiplier)).setScale(Rates.DEFAULT_ROUND_SCALE, Rates.DEFAULT_ROUNDING).toString()
        val bgnResult = BigDecimal(bgnVal2).multiply(BigDecimal(multiplier)).setScale(Rates.DEFAULT_ROUND_SCALE, Rates.DEFAULT_ROUNDING).toString()
        val brlResult = BigDecimal(brlVal2).multiply(BigDecimal(multiplier)).setScale(Rates.DEFAULT_ROUND_SCALE, Rates.DEFAULT_ROUNDING).toString()

        assert(resultMap3[CurrenciesEnum.AUD] == audResult)
        assert(resultMap3[CurrenciesEnum.BGN] == bgnResult)
        assert(resultMap3[CurrenciesEnum.BRL] == brlResult)


        val resultMap4 = viewModel.updateConvertedRates(rates2, viewModel.convertedRatesLiveData.value!!, multiplier)

        assert(resultMap4[CurrenciesEnum.EUR] == multiplier)
        assert(resultMap4[CurrenciesEnum.AUD] == audResult)
        assert(resultMap4[CurrenciesEnum.BGN] == bgnResult)
        assert(resultMap4[CurrenciesEnum.BRL] == brlResult)
    }

    @Test
    fun checkCurrencyMovedToTop() {
        val multiplier = "4"

        viewModel.currencyMultiplier = multiplier
        viewModel.baseCurrency = CurrenciesEnum.EUR
        viewModel.convertedRatesLiveData.value = viewModel.generateConvertedRatesMap(rates.base, rates.ratesEnumMap)

        viewModel.moveCurrencyToTopOfMap(CurrenciesEnum.BGN, BGN_VAL)

        assert(viewModel.convertedRatesLiveData.value!!.keys.indexOf(CurrenciesEnum.BGN) == 0)
        assert(viewModel.convertedRatesLiveData.value!![CurrenciesEnum.BGN] == BGN_VAL)
        assert(viewModel.convertedRatesLiveData.value!!.keys.indexOf(CurrenciesEnum.EUR) == 1)
    }

    @Test
    fun ratesArePredictingSuccessfully() {
        viewModel.rates = rates
        viewModel.baseCurrency = CurrenciesEnum.BGN

        rates = viewModel.generatePredictedRates()

        val rateEur = BigDecimal("1").divide(BigDecimal(BGN_VAL), Rates.DEFAULT_ROUND_SCALE, Rates.DEFAULT_ROUNDING).toString()
        val rateAud = BigDecimal(AUD_VAL).divide(BigDecimal(BGN_VAL), Rates.DEFAULT_ROUND_SCALE, Rates.DEFAULT_ROUNDING).toString()
        val rateBrl = BigDecimal(BRL_VAL).divide(BigDecimal(BGN_VAL), Rates.DEFAULT_ROUND_SCALE, Rates.DEFAULT_ROUNDING).toString()

        assert(rates.ratesEnumMap[CurrenciesEnum.EUR] == rateEur)
        assert(rates.ratesEnumMap[CurrenciesEnum.AUD] == rateAud)
        assert(rates.ratesEnumMap[CurrenciesEnum.BRL] == rateBrl)
    }
}