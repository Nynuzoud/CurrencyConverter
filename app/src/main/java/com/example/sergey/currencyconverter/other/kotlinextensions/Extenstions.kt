package com.example.sergey.currencyconverter.other.kotlinextensions

import androidx.lifecycle.MutableLiveData

fun <T : Any> MutableLiveData<T>.default(initialValue: T) = apply { value = initialValue }