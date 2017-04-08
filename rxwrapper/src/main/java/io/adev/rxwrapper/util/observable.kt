package io.adev.rxwrapper.util

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import java.util.concurrent.atomic.AtomicInteger

fun <T> preferLastObservable(vararg sources: Observable<T>) =
        preferLastObservable(sources.toList())

private const val sentNothing = -1

fun <T> preferLastObservable(sources: List<Observable<T>>): Observable<T> {
    return Observable.create { emitter ->
        var lastIndex = sentNothing
        val errorsAndCompletesCount = AtomicInteger(0)

        sources.forEachIndexed { i, observable ->
            observable.subscribe(observer<T>({ value ->
                        synchronized(lastIndex) {
                            if (lastIndex < i) { // TODO прорефакторить
                                emitter.onNext(value)
                                lastIndex = i
                            }
                        }
                    }, {
                        errorsAndCompletesCount.incrementAndGet()

                        if (i == sources.lastIndex) {
                            emitter.onComplete()
                        }
                    }, { throwable ->
                        if (errorsAndCompletesCount.incrementAndGet() == sources.size && lastIndex == sentNothing) {
                            emitter.safeOnError(throwable)
                        } else {
                            throwable.printStackTrace()
                        }
                    }))
        }
    }
}

fun <T1, T2, R> combineLatestObservable(
        source1: ObservableSource<out T1>,
        source2: ObservableSource<out T2>,
        combiner: (T1, T2) -> R): Observable<R> {
    return Observable.combineLatest(
            source1,
            source2,
            BiFunction<T1, T2, R> { t1, t2 ->
                combiner(t1, t2)
            })
}

fun <T1, T2, T3, R> combineLatestObservable(
        source1: ObservableSource<out T1>,
        source2: ObservableSource<out T2>,
        source3: ObservableSource<out T3>,
        combiner: (T1, T2, T3) -> R): Observable<R> {
    return Observable.combineLatest(
            source1,
            source2,
            source3,
            Function3<T1, T2, T3, R> { t1, t2, t3 ->
                combiner(t1, t2, t3)
            })
}