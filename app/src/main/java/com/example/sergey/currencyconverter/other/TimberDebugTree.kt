package com.example.sergey.currencyconverter.other

import timber.log.Timber

class TimberDebugTree : Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement): String? {
        return String
                .format("[%s#%s:%s]",
                        element.lineNumber,
                        element.methodName,
                        super.createStackElementTag(element))
    }
}