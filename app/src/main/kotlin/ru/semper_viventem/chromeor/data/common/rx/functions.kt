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

/**
 * Выполнить асинхронную итерацию через UseCase
 *
 * @param observableFactory сам вариант использования. Использовать [RxAdapter]
 * @param observeScheduler поток в котором итерация выполняется. По умолчанию [DEFAULT_OBSERVE_SCHEDULER]
 */
fun <T, C> asyncUseCase(observableFactory: ObservableFactory<T, C>,
                        observeScheduler: Scheduler): UseCase<T, C> {
    return useCase(observableFactory,
            observeScheduler = observeScheduler,
            subscribeScheduler = DEFAULT_SUBSCRIBE_SCHEDULER)
}

/**
 * Выполнить асинхронную итерацию через UseCase
 *
 * @param observableFactory вариант использования. Использовать [RxAdapter]
 */
fun <T, C> asyncUseCase(observableFactory: ObservableFactory<T, C>): UseCase<T, C> {
    return useCase(observableFactory,
            observeScheduler = DEFAULT_OBSERVE_SCHEDULER,
            subscribeScheduler = DEFAULT_SUBSCRIBE_SCHEDULER)
}