package ru.semper_viventem.chromeor.presentation.presenter

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.semper_viventem.chromeor.domain.CopyrateRootManager
import ru.semper_viventem.chromeor.domain.GetLoginFromDB
import ru.semper_viventem.chromeor.model.LoginEntity
import ru.semper_viventem.chromeor.presentation.view.main.MainView
import rx.lang.kotlin.subscriber

/**
 * @author Kulikov Konstantin
 * @since 09.02.2017.
 */

@InjectViewState
class MainPresenter: MvpPresenter<MainView>() {

    private var mLoginList: List<LoginEntity> = emptyList()

    fun loadDatabase(context: Context) {

        CopyrateRootManager(context).exequte(subscriber<Int>().onNext { exitCode ->
            GetLoginFromDB(context).exequte(subscriber<List<LoginEntity>>().onNext { loginEntityList ->
                mLoginList = loginEntityList
                viewState.onDatabaseLoaded(mLoginList)
            }.onError {
                viewState.onErrorLoadingDB()
            })
        }.onError {
            viewState.onErrorCopyrateDB()
        })
    }

    fun searchFromList(query: String) {
        val result = mLoginList.filter { (it.originUrl.contains(query) ||
                it.actionUrl.contains(query) ||
                it.passwordValue.contains(query)) ||
                it.usernameValue.contains(query)}
        viewState.onDatabaseLoaded(result)
    }
}