package ru.semper_viventem.chromeor.domain.iteractor

import io.adev.rxwrapper.RxAdapter
import io.reactivex.ObservableEmitter
import ru.semper_viventem.chromeor.domain.store.YandexDataStore
import ru.semper_viventem.chromeor.presentation.model.LoginEntity
import javax.inject.Inject

/**
 * @author Kulikov Konstantin
 * @since 08.04.2017.
 */
class GetYandexLoginList @Inject constructor(
        private val mYandexDataStore: YandexDataStore
) : RxAdapter<List<LoginEntity>, Void>() {
    override fun execute(emitter: ObservableEmitter<List<LoginEntity>>, criteria: Void?) {

        mYandexDataStore.copyData()
        val result = mYandexDataStore.getData()

        emitter.onNext(result)
    }
}