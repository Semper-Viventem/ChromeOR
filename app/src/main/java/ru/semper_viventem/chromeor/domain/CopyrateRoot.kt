package ru.semper_viventem.chromeor.domain

import rx.Subscriber

/**
 * @author Kulikov Konstantin
 * @since 24.01.2017.
 */
interface CopyrateRoot {

    companion object {
        val NO_ROOT = 255
    }

    fun exequte(subscribe: Subscriber<Int>)
}