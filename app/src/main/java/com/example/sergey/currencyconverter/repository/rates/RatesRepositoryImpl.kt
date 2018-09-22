package com.example.sergey.currencyconverter.repository.rates

import com.example.sergey.currencyconverter.api.Api
import com.example.sergey.currencyconverter.other.exceptions.RatesException
import com.example.sergey.currencyconverter.ui.rates.Rates
import com.example.sergey.currencyconverter.ui.vo.RatesVO
import com.example.sergey.currencyconverter.ui.vo.RatesVOMapper
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Repository that handles [RatesDTO]
 * Methods return completable and write data to cache
 */

class RatesRepositoryImpl(private val api: Api) : RatesRepository {

    override fun getRatesRepository(base: Rates) : Completable {

        return api
                .getLatestRates(base.name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(RatesVOMapper())
                .flatMapCompletable {
                    if ((it.ratesMap?.size ?: 0) > 0) {
                        return@flatMapCompletable Completable.fromObservable(Observable.just(it))
                    } else {
                        return@flatMapCompletable Completable.fromObservable(Observable.error<RatesVO>(RatesException("Rates is null or rates map is null or empty")))
                    }
                }
    }
}