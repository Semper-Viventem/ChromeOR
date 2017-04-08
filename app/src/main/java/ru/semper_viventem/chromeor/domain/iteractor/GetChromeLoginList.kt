package ru.semper_viventem.chromeor.domain.iteractor

import io.adev.rxwrapper.RxAdapter
import io.reactivex.ObservableEmitter
import ru.semper_viventem.chromeor.data.repository.chrome.ChromeDataStore
import ru.semper_viventem.chromeor.presentation.model.LoginEntity
import javax.inject.Inject

/**
 * @author Kulikov Konstantin
 * @since 08.04.2017.
 */

class GetChromeLoginList @Inject constructor(
        private val mChromeDataStore: ChromeDataStore
) : RxAdapter<List<LoginEntity>, Void>() {

    override fun execute(emitter: ObservableEmitter<List<LoginEntity>>, criteria: Void?) {

        mChromeDataStore.copyData()
        val result = mChromeDataStore.getData()

        emitter.onNext(result)
    }
}
