package ru.semper_viventem.chromeor.presentation.view.main

import com.arellomobile.mvp.MvpView
import ru.semper_viventem.chromeor.presentation.model.LoginEntity

/**
 * @author Kulikov Konstantin
 * @since 09.02.2017.
 */
interface MainView: MvpView {

    /**
     * Список аккаунтов пользователя загружен
     *
     * @param passList список аккаунтов [LoginEntity]
     */
    fun onDatabaseLoaded(passList: List<LoginEntity>)

    /**
     * Начинается загрузка данных.
     * Показать анимацию загрузки
     */
    fun onBeginLoadingDB()

    /**
     * Ошибка при выгрузки базы
     */
    fun onErrorLoadingDB()

    /**
     * Ошибка при копировании базы
     */
    fun onErrorCopyingDB()
}