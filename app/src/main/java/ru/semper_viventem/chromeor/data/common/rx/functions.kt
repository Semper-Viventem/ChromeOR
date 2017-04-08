package ru.semper_viventem.chromeor.data.common.rx

import io.adev.rxwrapper.ObservableFactory
import io.adev.rxwrapper.UseCase
import io.adev.rxwrapper.util.observer
import io.adev.rxwrapper.util.safeOnError
import io.adev.rxwrapper.util.useCase
import io.reactivex.ObservableEmitter
import io.reactivex.Observer
import io.reactivex.Scheduler

/**
 * @author Kulikov Konstantin
 * @since 08.04.2017.
 */

val DEFAULT_OBSERVE_SCHEDULER: Scheduler = RxSchedulers().mainThread
val DEFAULT_SUBSCRIBE_SCHEDULER: Scheduler = RxSchedulers().io

fun <T, C> asyncUseCase(observableFactory: ObservableFactory<T, C>,
                        observeScheduler: Scheduler): UseCase<T, C> {
    return useCase(observableFactory,
            observeScheduler = observeScheduler,
            subscribeScheduler = DEFAULT_SUBSCRIBE_SCHEDULER)
}

fun <T, C> asyncUseCase(observableFactory: ObservableFactory<T, C>): UseCase<T, C> {
    return useCase(observableFactory,
            observeScheduler = DEFAULT_OBSERVE_SCHEDULER,
            subscribeScheduler = DEFAULT_SUBSCRIBE_SCHEDULER)
}

fun <T> observer(emitter: ObservableEmitter<T>): Observer<T> {
    return observer({ result ->
        emitter.onNext(result)
    }, {
        emitter.onComplete()
    }, { error ->
        emitter.safeOnError(error)
    })
}