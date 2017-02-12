package ru.semper_viventem.chromeor.domain.iteractor

import ru.semper_viventem.chromeor.data.repository.chrome.ChromeDataStore
import ru.semper_viventem.chromeor.domain.executor.PostExecutionThread
import ru.semper_viventem.chromeor.domain.executor.ThreadExecutor
import rx.Observable
import rx.Subscriber
import javax.inject.Inject


/**
 * @author Kulikov Konstantin
 * @since 24.01.2017.
 */
class CopyrateChromeDatabase @Inject constructor(
        private val mChromeDataStore: ChromeDataStore,
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread
): UseCase<CopyrateChromeDatabase.Arguments, Int>(threadExecutor, postExecutionThread) {

    class Arguments

    fun execute(useCaseSubscriber: Subscriber<Int>) {
        execute(Arguments(), useCaseSubscriber)
    }

    override fun buildObservable(argument: Arguments): Observable<Int> {
        return mChromeDataStore.copyData()
    }
}