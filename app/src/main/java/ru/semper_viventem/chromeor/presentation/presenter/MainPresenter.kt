package ru.semper_viventem.chromeor.presentation.presenter

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker
import io.adev.rxwrapper.util.observer
import ru.semper_viventem.chromeor.R
import ru.semper_viventem.chromeor.data.common.rx.asyncUseCase
import ru.semper_viventem.chromeor.domain.iteractor.GetChromeBetaLoginList
import ru.semper_viventem.chromeor.domain.iteractor.GetChromeLoginList
import ru.semper_viventem.chromeor.domain.iteractor.GetYandexLoginList
import ru.semper_viventem.chromeor.presentation.model.LoginEntity
import ru.semper_viventem.chromeor.presentation.view.main.MainView
import ru.semper_viventem.chromeor.util.App
import javax.inject.Inject

/**
 * @author Kulikov Konstantin
 * @since 09.02.2017.
 */

@InjectViewState
class MainPresenter: MvpPresenter<MainView>() {

    @Inject
    lateinit var mTracker: Tracker
    @Inject
    lateinit var mContext: Context

    @Inject
    lateinit var mGetChromeLoginList: GetChromeLoginList
    @Inject
    lateinit var mGetChromeBetaLoginList: GetChromeBetaLoginList
    @Inject
    lateinit var mGetYandexLoginList: GetYandexLoginList

    private var mLoginList: List<LoginEntity> = emptyList()

    init {
        App.component.inject(this)
    }

    fun loadChromeLoginList() {
        viewState.onBeginLoadingDB()
        asyncUseCase(mGetChromeLoginList).execute(observer({ loginList ->
            viewState.onDatabaseLoaded(loginList)
            trackerOnDBLoaded()
        }, {
            viewState.onErrorLoadingDB()
            trackerOnErrorLoaded()
        }))
    }

    fun loadChromeBetaLoginList() {
        viewState.onBeginLoadingDB()
        asyncUseCase(mGetChromeBetaLoginList).execute(observer({ loginList ->
            viewState.onDatabaseLoaded(loginList)
        }, {
            viewState.onErrorLoadingDB()
            trackerOnErrorLoaded()
        }))
    }

    fun searchFromList(query: String) {
        val result = mLoginList.filter { (it.originUrl.contains(query) ||
                it.actionUrl.contains(query) ||
                it.passwordValue.contains(query)) ||
                it.usernameValue.contains(query)}
        viewState.onDatabaseLoaded(result)
    }

    fun trackerOpenActivity() {
        mTracker.setScreenName(mContext.resources.getString(R.string.tracker_activity_title))
        mTracker.send(HitBuilders.ScreenViewBuilder().build())
    }

    fun trackerOnDBLoaded() {
        mTracker.send(HitBuilders.EventBuilder()
                .setCategory(mContext.resources.getString(R.string.tracker_onComplite_title))
                .setAction(mContext.resources.getString(R.string.tracker_onComplite))
                .build())
    }

    fun trackerOnErrorLoaded() {
        mTracker.send(HitBuilders.EventBuilder()
                .setCategory(mContext.resources.getString(R.string.tracker_onError_title))
                .setAction(mContext.resources.getString(R.string.tracker_onError))
                .build())
    }

    fun trackerOnErrorCopyrateDB() {
        mTracker.send(HitBuilders.EventBuilder()
                .setCategory(mContext.resources.getString(R.string.tracker_onError_title))
                .setAction(mContext.resources.getString(R.string.tracker_not_root))
                .build())
    }

    fun shareLoginData(loginModel: LoginEntity) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        val textToSend =
                "action_url: ${loginModel.actionUrl}\n" +
                        "origin_url: ${loginModel.originUrl}\n" +
                        "username: ${loginModel.usernameValue}\n" +
                        "password: ${loginModel.passwordValue}"
        intent.putExtra(Intent.EXTRA_TEXT, textToSend)

        try {
            mContext.startActivity(Intent.createChooser(intent, "Share"))
        } catch (ex: ActivityNotFoundException) {
            //TODO обработка ошибок
            ex.printStackTrace()
        }
    }
}