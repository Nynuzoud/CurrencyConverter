package com.example.sergey.currencyconverter.viewmodel

import com.example.sergey.currencyconverter.di.components.AppComponent
import com.example.sergey.currencyconverter.other.preferences.Preferences
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
import org.mockito.junit.MockitoJUnit
import java.util.*

@RunWith(JUnit4::class)
class MainViewModelUnitTest {

    @get:Rule
    val mockitoRule = MockitoJUnit.rule()

    private lateinit var rates: Rates

    private val AUD_VAL = 1.6191f
    private val BGN_VAL = 1.9591f
    private val BRL_VAL = 1.7998f

    private val appComponent = mock(AppComponent::class.java)
    private val ratesRepository = mock(RatesRepository::class.java)
    private val preferences = mock(Preferences::class.java)
    private var viewModel = MainViewModel(appComponent, ratesRepository, preferences)

    @Before
    fun init() {

        val enumMap = EnumMap<CurrenciesEnum, Float>(CurrenciesEnum::class.java)
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

        var multiplier = 1f
        viewModel.convertedRatesMap = viewModel.generateConvertedRatesMap(rates.base, rates.ratesEnumMap)

        val resultMap = viewModel.updateConvertedRates(rates, viewModel.convertedRatesMap, multiplier)

        assert(resultMap[CurrenciesEnum.AUD] == AUD_VAL)
        assert(resultMap[CurrenciesEnum.BGN] == BGN_VAL)
        assert(resultMap[CurrenciesEnum.BRL] == BRL_VAL)

        val audVal2 = 2.4815f
        val bgnVal2 = 2.8186f
        val brlVal2 = 2.1368f

        val enumMap = EnumMap<CurrenciesEnum, Float>(CurrenciesEnum::class.java)
        enumMap[CurrenciesEnum.AUD] = audVal2
        enumMap[CurrenciesEnum.BGN] = bgnVal2
        enumMap[CurrenciesEnum.BRL] = brlVal2

        val rates2 = Rates(
                base = CurrenciesEnum.EUR,
                ratesEnumMap = enumMap
        )

        val resultMap2 = viewModel.updateConvertedRates(rates2, viewModel.convertedRatesMap, multiplier)

        assert(resultMap2[CurrenciesEnum.AUD] == audVal2)
        assert(resultMap2[CurrenciesEnum.BGN] == bgnVal2)
        assert(resultMap2[CurrenciesEnum.BRL] == brlVal2)

        multiplier = 4.34f

        val resultMap3 = viewModel.updateConvertedRates(rates2, viewModel.convertedRatesMap, multiplier)

        assert(resultMap3[CurrenciesEnum.AUD] == audVal2 * multiplier)
        assert(resultMap3[CurrenciesEnum.BGN] == bgnVal2 * multiplier)
        assert(resultMap3[CurrenciesEnum.BRL] == brlVal2 * multiplier)
    }

    @Test
    fun checkCurrencyMovedToTop() {
        val multiplier = 4f

        viewModel.convertedRatesMap = viewModel.generateConvertedRatesMap(rates.base, rates.ratesEnumMap)

        viewModel.currencyMultiplier = multiplier
        viewModel.baseCurrency = CurrenciesEnum.EUR

        viewModel.moveCurrencyToTopOfMap(CurrenciesEnum.BGN, BGN_VAL)

        assert(viewModel.convertedRatesMap.keys.indexOf(CurrenciesEnum.BGN) == 0)
        assert(viewModel.convertedRatesMap[CurrenciesEnum.BGN] == BGN_VAL)
        assert(viewModel.convertedRatesMap.keys.indexOf(CurrenciesEnum.EUR) == 1)
        assert(viewModel.convertedRatesMap[CurrenciesEnum.EUR] == multiplier)
    }
}