package com.example.sergey.currencyconverter

import com.example.sergey.currencyconverter.api.Api
import com.example.sergey.currencyconverter.api.rates.RatesDTO
import com.example.sergey.currencyconverter.repository.rates.RatesRepositoryImpl
import com.example.sergey.currencyconverter.ui.rates.Rates
import com.example.sergey.currencyconverter.ui.vo.RatesVO
import com.example.sergey.currencyconverter.ui.vo.RatesVOMapper
import io.reactivex.Observable
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import timber.log.Timber
import java.lang.Exception
import java.util.*

class RatesRepositoryTest : BaseTest() {

    private lateinit var ratesDTO: RatesDTO
    private lateinit var ratesVO: RatesVO
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

    private val AUD_VAL = 1.6191f
    private val BGN_VAL = 1.9591f
    private val BRL_VAL = 4.7998f

    @Before
    fun init() {
        val enumMap = EnumMap<Rates, Float>(Rates::class.java)
        enumMap[Rates.AUD] = AUD_VAL
        enumMap[Rates.BGN] = BGN_VAL
        enumMap[Rates.BRL] = BRL_VAL

        ratesDTO = RatesDTO(
                base = "EUR",
                date = "2018-09-06",
                ratesMap = enumMap
        )

        ratesEmptyMap = RatesDTO(
                base = "EUR",
                date = "2018-09-06",
                ratesMap = EnumMap<Rates, Float>(Rates::class.java)
        )

        ratesVO = RatesVOMapper().apply(ratesDTO)
    }

    @Test
    fun checkRatesRepositoryCompletableFinishesSuccessfully() {
        `when`(api.getLatestRates("EUR")).thenReturn(Observable.fromArray(ratesDTO))

        var error: Throwable? = null

        ratesRepository
                .getRatesRepository(Rates.EUR)
                .subscribe({}, {error = it})

        assert(error == null)
    }

    @Test
    fun checkRatesRepositoryCompletableFinishesWithError() {
        `when`(api.getLatestRates("EUR")).thenReturn(Observable.fromArray(ratesEmptyMap))

        var error: Throwable? = null

        ratesRepository
                .getRatesRepository(Rates.EUR)
                .subscribe({}, {error = it})

        assert(error != null)
    }
}