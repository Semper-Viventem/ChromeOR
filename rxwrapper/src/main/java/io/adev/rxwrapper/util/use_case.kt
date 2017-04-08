package io.adev.rxwrapper.util

import io.adev.rxwrapper.ObservableFactory
import io.adev.rxwrapper.UseCase
import io.reactivex.Observable
import io.reactivex.Scheduler

fun <T, C> useCase(
        observableFactory: ObservableFactory<T, C>,
        observeScheduler: Scheduler? = null,
        subscribeScheduler: Scheduler? = null
): UseCase<T, C> {

    return object : UseCase<T, C>() {

        init {
            observeOn(observeScheduler)
            subscribeOn(subscribeScheduler)
        }

        private val mFactory = observableFactory

        override fun buildObservable(criteria: C?): Observable<T> {
            return mFactory.create(criteria)
        }

    }

}