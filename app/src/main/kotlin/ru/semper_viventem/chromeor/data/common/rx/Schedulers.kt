package ru.semper_viventem.chromeor.data.common.rx

import io.reactivex.Scheduler

/**
 * @author Kulikov Konstantin
 * @since 08.04.2017.
 */

interface Schedulers {
    val io: Scheduler
    val mainThread: Scheduler
    val computation: Scheduler
}