package ru.semper_viventem.chromeor.domain.iteractor

import ru.semper_viventem.chromeor.data.repository.chrome.ChromeDataStore
import ru.semper_viventem.chromeor.domain.executor.PostExecutionThread
import ru.semper_viventem.chromeor.domain.executor.ThreadExecutor
import ru.semper_viventem.chromeor.presentation.model.LoginEntity
import rx.Observable
import rx.Subscriber
import javax.inject.Inject


/**
 * @author Kulikov Konstantin
 * @since 24.01.2017.
 */
class GetChromeDatabase @Inject constructor(
        private val mChromeDataStore: ChromeDataStore,
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread
): UseCase<GetChromeDatabase.Arguments, List<LoginEntity>>(threadExecutor, postExecutionThread) {

    class Arguments

    override fun buildObservable(argument: Arguments): Observable<List<LoginEntity>> {
        return mChromeDataStore.getData()
    }

    fun execute(useCaseSubscriber: Subscriber<List<LoginEntity>>) {
        execute(Arguments(), useCaseSubscriber)
    }
}