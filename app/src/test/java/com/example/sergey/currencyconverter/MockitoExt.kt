
package com.example.sergey.currencyconverter

import org.mockito.Mockito

/**
 * a kotlin friendly mock that handles generics
 */
inline fun <reified T> mock(): T = Mockito.mock(T::class.java)