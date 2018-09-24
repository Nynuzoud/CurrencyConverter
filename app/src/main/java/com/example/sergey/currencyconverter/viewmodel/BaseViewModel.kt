package com.example.sergey.currencyconverter.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {

    private val subscriptions = CompositeDisposable()

    protected fun subscribe(disposable: Disposable) {
        subscriptions.add(disposable)
    }

    protected fun unsubscribe(disposable: Disposable) {
        if (!disposable.isDisposed) {
            subscriptions.remove(disposable)
        }
    }

    protected fun clearSubscriptions() {
        subscriptions.clear()
    }
}