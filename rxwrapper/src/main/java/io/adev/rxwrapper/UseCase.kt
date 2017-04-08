package io.adev.rxwrapper

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.observers.DisposableObserver

abstract class UseCase<T, in C> {

    private var mObserveScheduler: Scheduler? = null
    private var mSubscribeScheduler: Scheduler? = null
    private var mDisposable: Disposable = Disposables.empty()

    fun observeOn(observeScheduler: Scheduler?): UseCase<T, C> {
        mObserveScheduler = observeScheduler
        return this
    }

    fun subscribeOn(subscribeScheduler: Scheduler?): UseCase<T, C> {
        mSubscribeScheduler = subscribeScheduler
        return this
    }

    open fun execute(observer: DisposableObserver<T>, criteria: C? = null) {
        var observable = buildObservable(criteria)
        if (mObserveScheduler != null) {
            observable = observable.observeOn(mObserveScheduler)
        }
        if (mSubscribeScheduler != null) {
            observable = observable.subscribeOn(mSubscribeScheduler)
        }
        mDisposable = observable.subscribeWith(observer)
    }

    protected abstract fun buildObservable(criteria: C?): Observable<T>

    fun dispose() {
        if (!mDisposable.isDisposed) {
            mDisposable.dispose()
        }
    }

}