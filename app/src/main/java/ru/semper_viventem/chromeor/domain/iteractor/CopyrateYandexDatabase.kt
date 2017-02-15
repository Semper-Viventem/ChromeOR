package ru.semper_viventem.chromeor.domain.iteractor

import ru.semper_viventem.chromeor.data.repository.yandex.YandexDataStore
import ru.semper_viventem.chromeor.domain.executor.PostExecutionThread
import ru.semper_viventem.chromeor.domain.executor.ThreadExecutor
import rx.Observable
import rx.Subscriber
import javax.inject.Inject

/**
 * @author Kulikov Konstantin
 * @since 15.02.2017.
 */
class CopyrateYandexDatabase @Inject constructor(
        private val mYandexDataStore: YandexDataStore,
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread
): UseCase<CopyrateYandexDatabase.Arguments, Int>(threadExecutor, postExecutionThread) {

    class Arguments

    fun execute(useCaseSubscriber: Subscriber<Int>) {
        execute(Arguments(), useCaseSubscriber)
    }

    override fun buildObservable(argument: Arguments): Observable<Int> {
        return mYandexDataStore.copyData()
    }
}