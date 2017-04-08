package ru.semper_viventem.chromeor.data.repository.yandex

import ru.semper_viventem.chromeor.presentation.model.LoginEntity

/**
 * @author Kulikov Konstantin
 * @since 15.02.2017.
 */
interface YandexDataStore {
    companion object {
        val origin_url = "origin_url"
        val action_url = "action_url"
        val username_value = "username_value"
        val password_value = "password_value"

        val DB_PACKAGE = "/data/data/com.yandex.browser/app_chromium/Default/Login\\ Data"
        val DB_NAME = "yandex_login_data.db"
    }

    fun copyData(): Int
    fun getData(): List<LoginEntity>
}