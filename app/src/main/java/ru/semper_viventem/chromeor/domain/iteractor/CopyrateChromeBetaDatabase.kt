package ru.semper_viventem.chromeor.domain.iteractor

import ru.semper_viventem.chromeor.data.repository.chrome_beta.ChromeBetaDataStore
import ru.semper_viventem.chromeor.domain.executor.PostExecutionThread
import ru.semper_viventem.chromeor.domain.executor.ThreadExecutor
import rx.Observable
import rx.Subscriber
import javax.inject.Inject

/**
 * @author Kulikov Konstantin
 * @since 18.02.2017.
 */
class CopyrateChromeBetaDatabase @Inject constructor(
        private val mChromeBetaDataStore: ChromeBetaDataStore,
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread
): UseCase<CopyrateChromeBetaDatabase.Arguments, Int>(threadExecutor, postExecutionThread) {

    override fun buildObservable(argument: Arguments): Observable<Int> {
        return mChromeBetaDataStore.copyData()
    }

    fun execute(useCaseSubscriber: Subscriber<Int>) {
        execute(Arguments(), useCaseSubscriber)
    }

    class Arguments
}