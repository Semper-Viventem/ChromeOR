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

    /**
     * Копирование БД
     *
     * @return код результата итерации [Int]
     */
    fun copyData(): Int

    /**
     * Получить список пользовательских
     * аккаунтов из БД
     *
     * @return список [List] аккаунтов пользователя [LoginEntity]
     */
    fun getData(): List<LoginEntity>
}