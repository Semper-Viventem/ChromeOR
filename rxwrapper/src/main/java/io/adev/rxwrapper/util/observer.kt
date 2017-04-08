package io.adev.rxwrapper.util

import io.reactivex.observers.DisposableObserver

fun <T> observer(onNext: (T) -> Unit, onError: (Throwable) -> Unit) =
        object : DisposableObserver<T>() {
            override fun onNext(value: T) {
                onNext(value)
            }
            override fun onError(throwable: Throwable) {
                try {
                    onError(throwable)
                } catch (exception: Throwable) {
                    exception.printStackTrace()
                }
            }
            override fun onComplete() { }
        }

fun <T> observer(onNext: (T) -> Unit, onComplete: () -> Unit, onError: (Throwable) -> Unit) =
        object : DisposableObserver<T>() {
            override fun onNext(value: T) {
                onNext(value)
            }
            override fun onComplete() {
                onComplete()
            }
            override fun onError(throwable: Throwable) {
                try {
                    onError(throwable)
                } catch (exception: Throwable) {
                    exception.printStackTrace()
                }
            }
        }