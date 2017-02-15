package ru.semper_viventem.chromeor.domain.iteractor

import ru.semper_viventem.chromeor.data.repository.yandex.YandexDataStore
import ru.semper_viventem.chromeor.domain.executor.PostExecutionThread
import ru.semper_viventem.chromeor.domain.executor.ThreadExecutor
import ru.semper_viventem.chromeor.presentation.model.LoginEntity
import rx.Observable
import rx.Subscriber
import javax.inject.Inject

/**
 * @author Kulikov Konstantin
 * @since 15.02.2017.
 */
class GetYandexDatabase @Inject constructor(
        private val mYandexDataStore: YandexDataStore,
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread
): UseCase<GetYandexDatabase.Arguments, List<LoginEntity>>(threadExecutor, postExecutionThread) {

    class Arguments

    override fun buildObservable(argument: Arguments): Observable<List<LoginEntity>> {
        return mYandexDataStore.getData()
    }

    fun execute(useCaseSubscriber: Subscriber<List<LoginEntity>>) {
        execute(Arguments(), useCaseSubscriber)
    }
}