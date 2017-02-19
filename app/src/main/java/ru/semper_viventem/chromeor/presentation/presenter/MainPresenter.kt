package ru.semper_viventem.chromeor.presentation.presenter

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker
import ru.semper_viventem.chromeor.R
import ru.semper_viventem.chromeor.domain.iteractor.CopyrateChromeBetaDatabase
import ru.semper_viventem.chromeor.domain.iteractor.CopyrateChromeDatabase
import ru.semper_viventem.chromeor.domain.iteractor.GetChromeBetaDatabase
import ru.semper_viventem.chromeor.domain.iteractor.GetChromeDatabase
import ru.semper_viventem.chromeor.presentation.model.LoginEntity
import ru.semper_viventem.chromeor.presentation.view.main.MainView
import ru.semper_viventem.chromeor.util.App
import rx.lang.kotlin.subscriber
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
    lateinit var mCopyrateChromeDatabase: CopyrateChromeDatabase
    @Inject
    lateinit var mGetChromeDatabase: GetChromeDatabase

    @Inject
    lateinit var mCopyrateChromeBetaDatabase: CopyrateChromeBetaDatabase
    @Inject
    lateinit var mGetChromeBetaDatabase: GetChromeBetaDatabase

    private var mLoginList: List<LoginEntity> = emptyList()

    init {
        App.component.inject(this)
    }

    fun loadChromeData() {
        viewState.onBeginLoadingDB()
        mCopyrateChromeDatabase.execute(subscriber<Int>()
                .onNext { exitCode ->
                    mGetChromeDatabase.execute(subscriber<List<LoginEntity>>()
                            .onNext { loginEntityList ->
                                mLoginList = loginEntityList
                                viewState.onDatabaseLoaded(mLoginList)
                                trackerOnDBLoaded()
                            }
                            .onError {
                                viewState.onErrorLoadingDB()
                                trackerOnErrorLoaded()
                            })
                }
                .onError {
                    viewState.onErrorCopyrateDB()
                    trackerOnErrorCopyrateDB()
                })
    }

    fun loadChromeBetaDataba() {
        viewState.onBeginLoadingDB()
        mCopyrateChromeBetaDatabase.execute(subscriber<Int>()
                .onNext { exitCode ->
                    mGetChromeBetaDatabase.execute(subscriber<List<LoginEntity>>()
                            .onNext { loginEntityList ->
                                mLoginList = loginEntityList
                                viewState.onDatabaseLoaded(mLoginList)
                                trackerOnDBLoaded()
                            }
                            .onError {
                                viewState.onErrorLoadingDB()
                                trackerOnErrorLoaded()
                            })
                }
                .onError {
                    viewState.onErrorCopyrateDB()
                    trackerOnErrorCopyrateDB()
                })
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