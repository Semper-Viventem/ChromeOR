package io.adev.rxwrapper

import io.reactivex.Observable

interface ObservableFactory<T, in C> {

    fun create(criteria: C? = null): Observable<T>

}