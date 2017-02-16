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

        val DB_PACKAGE = "/data/data/com.android.chrome/app_chrome/Default/Login\\ Data"
        val DB_NAME = "c_login_data.db"
    }

    fun copyData(): Observable<Int>
    fun getData(): Observable<List<LoginEntity>>
}