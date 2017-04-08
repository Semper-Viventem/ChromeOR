package ru.semper_viventem.chromeor.data.common.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject
import javax.inject.Singleton
import io.reactivex.schedulers.Schedulers as ReactivexSchedulers

/**
 * @author Kulikov Konstantin
 * @since 08.04.2017.
 */
@Singleton
class RxSchedulers @Inject constructor() : Schedulers {

    override val io: Scheduler
        get() = ReactivexSchedulers.io()
    override val mainThread: Scheduler
        get() = AndroidSchedulers.mainThread()
    override val computation: Scheduler
        get() = ReactivexSchedulers.computation()
}