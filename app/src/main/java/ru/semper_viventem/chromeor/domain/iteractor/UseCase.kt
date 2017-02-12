package ru.semper_viventem.chromeor.domain.iteractor

import ru.semper_viventem.chromeor.domain.executor.PostExecutionThread
import ru.semper_viventem.chromeor.domain.executor.ThreadExecutor
import rx.Observable
import rx.Subscriber
import rx.schedulers.Schedulers
import rx.subscriptions.Subscriptions

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This interface represents a execution unit for different use cases (this means any use case
 * in the application should implement this contract).
 *
 * By convention each UseCase implementation will return the result using a {@link rx.Subscriber}
 * that will execute its job in a background th read and will post the result in the UI thread.
 *
 * @author Kulikov Konstantin
 * @since 12.02.2017.
 */
abstract class UseCase<Argument, Result> protected constructor(
        private val mThreadExecutor: ThreadExecutor,
        private val mPostExecutionThread: PostExecutionThread
) {

    private var mSubscription = Subscriptions.empty()

    /**
     * Builds an [rx.Observable] which will be used when executing the current [UseCase].
     */
    abstract fun buildObservable(argument: Argument): Observable<Result>

    /**
     * Executes the current use case.
     *
     * @param useCaseSubscriber The guy who will be listen to the observable build
     *          with [buildObservable].
     */
    open fun execute(argument: Argument, useCaseSubscriber: Subscriber<Result>) {
        mSubscription = buildObservable(argument)
                .subscribeOn(Schedulers.from(mThreadExecutor))
                .observeOn(mPostExecutionThread.getScheduler())
                .subscribe(useCaseSubscriber)
    }

    /**
     * Unsubscribes from current [rx.Subscription].
     */
    open fun unsubscribe() {
        if (!mSubscription.isUnsubscribed) {
            mSubscription.unsubscribe()
        }
    }

}