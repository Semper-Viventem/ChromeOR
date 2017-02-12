package ru.semper_viventem.chromeor.data.repository.chrome

import ru.semper_viventem.chromeor.presentation.model.LoginEntity
import rx.Observable

/**
 * @author Kulikov Konstantin
 * @since 12.02.2017.
 */
interface ChromeDataStore {
    companion object {
        val origin_url = "origin_url"
        val action_url = "action_url"
        val username_value = "username_value"
        val password_value = "password_value"
    }

    fun copyData(): Observable<Int>
    fun getData(): Observable<List<LoginEntity>>
}