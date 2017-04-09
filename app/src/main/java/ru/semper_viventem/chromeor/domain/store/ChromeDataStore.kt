package ru.semper_viventem.chromeor.domain.store

import ru.semper_viventem.chromeor.presentation.model.LoginEntity
import javax.inject.Singleton

/**
 * @author Kulikov Konstantin
 * @since 12.02.2017.
 */
@Singleton
interface ChromeDataStore {
    companion object {
        val origin_url = "origin_url"
        val action_url = "action_url"
        val username_value = "username_value"
        val password_value = "password_value"

        val DB_PACKAGE = "/data/data/com.android.chrome/app_chrome/Default/Login\\ Data"
        val DB_NAME = "chrome_login_data.db"
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