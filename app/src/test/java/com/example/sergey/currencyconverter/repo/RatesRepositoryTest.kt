package com.example.sergey.currencyconverter.repo

import com.example.sergey.currencyconverter.BaseTest
import com.example.sergey.currencyconverter.RxImmediateSchedulerRule
import com.example.sergey.currencyconverter.repository.api.Api
import com.example.sergey.currencyconverter.repository.api.rates.RatesDTO
import com.example.sergey.currencyconverter.repository.data.Rates
import com.example.sergey.currencyconverter.repository.data.RatesMapper
import com.example.sergey.currencyconverter.repository.interactor.rates.RatesRepositoryImpl
import com.example.sergey.currencyconverter.ui.rates.CurrenciesEnum
import io.reactivex.Single
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import java.util.*

class RatesRepositoryTest : BaseTest() {

    private lateinit var ratesDTO: RatesDTO
    private lateinit var rates: Rates
    private lateinit var ratesEmptyMap: RatesDTO

    companion object {
        @JvmStatic
        @get:ClassRule
        val schedulers = RxImmediateSchedulerRule()
    }

    @get:Rule
    val mockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var api: Api

    @InjectMocks
    lateinit var ratesRepository: RatesRepositoryImpl

    private val AUD_VAL = "1.61"
    private val BGN_VAL = "1.95"
    private val BRL_VAL = "4.79"

    @Before
    fun init() {
        val enumMap = EnumMap<CurrenciesEnum, String>(CurrenciesEnum::class.java)
        enumMap[CurrenciesEnum.AUD] = AUD_VAL
        enumMap[CurrenciesEnum.BGN] = BGN_VAL
        enumMap[CurrenciesEnum.BRL] = BRL_VAL

        ratesDTO = RatesDTO(
                base = CurrenciesEnum.EUR,
                date = "2018-09-06",
                ratesMap = enumMap
        )

        ratesEmptyMap = RatesDTO(
                base = CurrenciesEnum.EUR,
                date = "2018-09-06",
                ratesMap = EnumMap(CurrenciesEnum::class.java)
        )

        rates = RatesMapper().apply(ratesDTO)
    }

    @Test
    fun checkRatesRepositoryCompletableFinishesSuccessfully() {
        `when`(api.getLatestRates("EUR")).thenReturn(Single.just(ratesDTO))

        var result: Rates? = null
        var error: Throwable? = null

        ratesRepository
                .getRatesRepository(CurrenciesEnum.EUR)
                .subscribe({
                    result = it
                }, {error = it})

        assert(result != null)
        assert(error == null)
    }

    @Test
    fun checkRatesRepositoryCompletableFinishesWithError() {
        `when`(api.getLatestRates(CurrenciesEnum.EUR.name)).thenReturn(Single.just(ratesEmptyMap))

        var result: Rates? = null
        var error: Throwable? = null

        ratesRepository
                .getRatesRepository(CurrenciesEnum.EUR)
                .subscribe({
                    result = it
                }, {error = it})

        assert(result == null)
        assert(error != null)
    }
}