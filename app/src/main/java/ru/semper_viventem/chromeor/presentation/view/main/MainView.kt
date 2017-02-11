package ru.semper_viventem.chromeor.presentation.view.main

import com.arellomobile.mvp.MvpView
import ru.semper_viventem.chromeor.presentation.model.LoginEntity

/**
 * @author Kulikov Konstantin
 * @since 09.02.2017.
 */
interface MainView: MvpView {
    fun onDatabaseLoaded(passList: List<LoginEntity>)
    fun onBeginLoadingDB()
    fun onErrorLoadingDB()
    fun onErrorCopyrateDB()
}