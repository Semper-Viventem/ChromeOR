package ru.semper_viventem.chromeor.data.repository.chrome_beta

import ru.semper_viventem.chromeor.presentation.model.LoginEntity
import rx.Observable

/**
 * @author Kulikov Konstantin
 * @since 18.02.2017.
 */
interface ChromeBetaDataStore {
    companion object {
        val origin_url = "origin_url"
        val action_url = "action_url"
        val username_value = "username_value"
        val password_value = "password_value"

        val DB_PACKAGE = "/data/data/com.chrome.beta/app_chrome/Default/Login\\ Data"
        val DB_NAME = "chrome_beta_login_data.db"
    }

    fun copyData(): Observable<Int>
    fun getData(): Observable<List<LoginEntity>>
}