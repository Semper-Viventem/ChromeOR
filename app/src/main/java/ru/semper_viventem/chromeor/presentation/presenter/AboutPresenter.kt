package ru.semper_viventem.chromeor.presentation.presenter

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker
import ru.semper_viventem.chromeor.R
import ru.semper_viventem.chromeor.presentation.view.about.AboutView
import ru.semper_viventem.chromeor.util.App
import javax.inject.Inject

/**
 * @author Kulikov Konstantin
 * @since 11.02.2017.
 */
@InjectViewState
class AboutPresenter: MvpPresenter<AboutView>() {

    @Inject
    lateinit var mTracker: Tracker
    @Inject
    lateinit var mContext: Context

    init {
        App.component.inject(this)
    }

    fun trackerOpenActivity() {

        mTracker.setScreenName(mContext.resources.getString(R.string.tracker_about_activity_title))
        mTracker.send(HitBuilders.ScreenViewBuilder().build())
    }
}