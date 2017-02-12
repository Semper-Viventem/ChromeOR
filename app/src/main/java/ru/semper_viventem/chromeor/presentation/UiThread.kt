package ru.semper_viventem.chromeor.presentation

import ru.semper_viventem.chromeor.domain.executor.PostExecutionThread
import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Kulikov Konstantin
 * @since 12.02.2017
 */
@Singleton
class UiThread @Inject constructor() : PostExecutionThread {

    override fun getScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

}