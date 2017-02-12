package ru.semper_viventem.chromeor.data.executor

import ru.semper_viventem.chromeor.domain.executor.ThreadExecutor
import java.util.concurrent.*
import javax.inject.Inject
import javax.inject.Singleton


/**
 * @author Kulikov Konstanti
 * @since 12.02.2017
 */
@Singleton
class JobExecutor @Inject constructor() : ThreadExecutor {

    private val INITIAL_POOL_SIZE = 3
    private val MAX_POOL_SIZE = 5
    // Sets the amount of time an idle thread waits before terminating
    private val KEEP_ALIVE_TIME = 10L
    // Sets the Time Unit to seconds
    private val KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS

    private val mWorkQueue: BlockingQueue<Runnable>
    private val mThreadPoolExecutor: ThreadPoolExecutor
    private val mThreadFactory: ThreadFactory

    init {
        mWorkQueue = LinkedBlockingQueue()
        mThreadFactory = JobThreadFactory()
        mThreadPoolExecutor = ThreadPoolExecutor(INITIAL_POOL_SIZE, MAX_POOL_SIZE,
                KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, this.mWorkQueue, this.mThreadFactory)
    }

    override fun execute(runnable: Runnable) {
        mThreadPoolExecutor.execute(runnable)
    }

    private class JobThreadFactory : ThreadFactory {
        private var counter = 0

        override fun newThread(runnable: Runnable): Thread {
            return Thread(runnable, THREAD_NAME + counter++)
        }

        companion object {
            private val THREAD_NAME = "android_"
        }
    }

}