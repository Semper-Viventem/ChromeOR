package io.adev.rxwrapper.util

import io.reactivex.ObservableEmitter

fun ObservableEmitter<*>.safeOnError(throwable: Throwable) {
    if (!isDisposed) {
        onError(throwable)
    } else {
        throwable.printStackTrace()
    }
}