package io.adev.rxwrapper

import io.reactivex.Observable

abstract class AbsObservableFactory<T, in C> : ObservableFactory<T, C> {

    override fun create(criteria: C?): Observable<T> {
        return buildObservable(criteria)
    }

    protected abstract fun buildObservable(criteria: C?): Observable<T>

}