package ru.semper_viventem.chromeor.domain.iteractor

import io.adev.rxwrapper.RxAdapter
import io.reactivex.ObservableEmitter
import ru.semper_viventem.chromeor.data.repository.chrome_beta.ChromeBetaDataStore
import ru.semper_viventem.chromeor.presentation.model.LoginEntity
import javax.inject.Inject

/**
 * @author Kulikov Konstantin
 * @since 08.04.2017.
 */
class GetChromeBetaLoginList @Inject constructor(
        private val mChromeBetaDataStore: ChromeBetaDataStore
) : RxAdapter<List<LoginEntity>, Void>() {
    override fun execute(emitter: ObservableEmitter<List<LoginEntity>>, criteria: Void?) {

        mChromeBetaDataStore.copyData()
        val result = mChromeBetaDataStore.getData()

        emitter.onNext(result)
    }
}