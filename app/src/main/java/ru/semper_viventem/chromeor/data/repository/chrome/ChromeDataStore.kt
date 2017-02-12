package ru.semper_viventem.chromeor.data.repository.chrome

import ru.semper_viventem.chromeor.presentation.model.LoginEntity
import rx.Observable

/**
 * @author Kulikov Konstantin
 * @since 12.02.2017.
 */
interface ChromeDataStore {
    fun copyData(): Observable<Int>
    fun getData(): Observable<List<LoginEntity>>
}