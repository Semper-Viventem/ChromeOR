package ru.semper_viventem.chromeor.di.module

import android.content.Context
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.Tracker
import dagger.Module
import dagger.Provides
import ru.semper_viventem.chromeor.R
import ru.semper_viventem.chromeor.data.executor.JobExecutor
import ru.semper_viventem.chromeor.domain.executor.PostExecutionThread
import ru.semper_viventem.chromeor.domain.executor.ThreadExecutor
import ru.semper_viventem.chromeor.presentation.UiThread
import ru.semper_viventem.chromeor.util.App
import javax.inject.Singleton

/**
 * @author Kulikov Konstantin
 * @since 11.02.2017.
 */
@Module
class AppModule(
        private val mApp: App,
        private val mContext: Context
) {
    private var mTracker: Tracker? = null

    @Provides
    @Singleton
    fun provideApp(): App = mApp

    @Provides
    @Singleton
    fun provideContext(): Context = mContext

    @Provides
    @Singleton
    fun provideTracker(): Tracker {
        if (mTracker == null) {
            val analytics = GoogleAnalytics.getInstance(mContext)
            mTracker = analytics.newTracker(R.xml.global_tracker)
        }
        return mTracker!!
    }

    @Provides
    @Singleton
    fun provideThreadExecutor(jobExecutor: JobExecutor): ThreadExecutor = jobExecutor

    @Provides
    @Singleton
    fun providePostExecutionThread(uiThread: UiThread): PostExecutionThread = uiThread
}