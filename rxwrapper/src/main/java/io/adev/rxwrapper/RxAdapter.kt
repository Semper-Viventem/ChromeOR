package io.adev.rxwrapper

import io.adev.rxwrapper.util.safeOnError
import io.reactivex.Observable
import io.reactivex.ObservableEmitter

abstract class RxAdapter<T, in C> : AbsObservableFactory<T, C>() {

    /**
     * ?
     */
    override fun buildObservable(criteria: C?): Observable<T> {
        return Observable.create<T> { emitter ->
            try {
                execute(emitter, criteria)
                emitter.onComplete()
            } catch (throwable: Throwable) {
                emitter.safeOnError(throwable)
            }
        }
    }

    /**
     * ?
     */
    protected abstract fun execute(emitter: ObservableEmitter<T>, criteria: C?)

}