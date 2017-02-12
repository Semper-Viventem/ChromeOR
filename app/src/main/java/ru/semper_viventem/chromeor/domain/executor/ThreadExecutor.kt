package ru.semper_viventem.chromeor.domain.executor

import java.util.concurrent.Executor

/**
 * Executor implementation can be based on different frameworks or techniques of asynchronous
 * execution, but every implementation will execute the
 * [ru.semper_viventem.chromeor.domain.iteractor.UseCase] out of the UI thread.
 *
 * @author Kulikov Konstantin
 * @since 12.02.2017
 */
interface ThreadExecutor : Executor