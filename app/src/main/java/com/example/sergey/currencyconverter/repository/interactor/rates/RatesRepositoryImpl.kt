package com.example.sergey.currencyconverter.repository.interactor.rates

import com.example.sergey.currencyconverter.other.exceptions.RatesException
import com.example.sergey.currencyconverter.repository.api.Api
import com.example.sergey.currencyconverter.repository.data.Rates
import com.example.sergey.currencyconverter.repository.data.RatesMapper
import com.example.sergey.currencyconverter.ui.rates.CurrenciesEnum
import io.reactivex.Observable

/**
 * Repository that handles [RatesDTO]
 * Methods return completable and write data to cache
 */

class RatesRepositoryImpl(private val api: Api) : RatesRepository {

    override fun getRatesRepository(base: CurrenciesEnum): Observable<Rates> {

//        return Observable
//                .interval(1, TimeUnit.SECONDS)
//                .flatMap {
//                    api.getLatestRates(base.name)
//                }
//                .map(RatesMapper())
//                .flatMap { rates ->
//                    if (rates.ratesEnumMap.isNotEmpty()) {
//                        Observable.just(rates)
//                    } else {
//                        Observable.error<Rates>(RatesException("CurrenciesEnum is null or rates map is null or empty"))
//                    }
//                }
//                .repeat()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())

        return api.getLatestRates(base.name)
                .map(RatesMapper())
                .flatMap {
                    if (it.ratesEnumMap.isNotEmpty()) {
                        Observable.just(it)
                    } else {
                        Observable.error<Rates>(RatesException("CurrenciesEnum is null or rates map is null or empty"))
                    }
                }
    }
}