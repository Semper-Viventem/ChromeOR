package ru.semper_viventem.chromeor.domain.iteractor

import ru.semper_viventem.chromeor.data.repository.chrome_beta.ChromeBetaDataStore
import ru.semper_viventem.chromeor.domain.executor.PostExecutionThread
import ru.semper_viventem.chromeor.domain.executor.ThreadExecutor
import ru.semper_viventem.chromeor.presentation.model.LoginEntity
import rx.Observable
import rx.Subscriber
import javax.inject.Inject

/**
 * @author Kulikov Konstantin
 * @since 18.02.2017.
 */
class GetChromeBetaDatabase @Inject constructor(
        private val mChromeBetaDataStore: ChromeBetaDataStore,
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread
): UseCase<GetChromeBetaDatabase.Arguments, List<LoginEntity>>(threadExecutor, postExecutionThread) {

    class Arguments

    override fun buildObservable(argument: Arguments): Observable<List<LoginEntity>> {
        return mChromeBetaDataStore.getData()
    }

    fun execute(useCaseSubscriber: Subscriber<List<LoginEntity>>) {
        execute(Arguments(), useCaseSubscriber)
    }
}