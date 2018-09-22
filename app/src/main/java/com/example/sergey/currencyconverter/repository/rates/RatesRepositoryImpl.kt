package com.example.sergey.currencyconverter.repository.rates

import com.example.sergey.currencyconverter.api.Api
import com.example.sergey.currencyconverter.ui.rates.Rates
import com.example.sergey.currencyconverter.ui.vo.RatesVO
import com.example.sergey.currencyconverter.ui.vo.RatesVOMapper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Repository that handles [RatesDTO]
 */

class RatesRepositoryImpl(private val api: Api) : RatesRepository {

    override fun getRatesRepository(base: Rates) : Observable<RatesVO> {

        return api
                .getLatestRates(base.name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(RatesVOMapper())
    }
}