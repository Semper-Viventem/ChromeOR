package ru.semper_viventem.chromeor.data.common.rx

import io.adev.rxwrapper.ObservableFactory
import io.adev.rxwrapper.UseCase
import io.adev.rxwrapper.util.useCase
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