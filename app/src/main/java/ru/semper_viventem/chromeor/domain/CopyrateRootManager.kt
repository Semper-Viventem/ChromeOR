package ru.semper_viventem.chromeor.domain

import android.content.Context
import ru.semper_viventem.chromeor.domain.CopyrateRoot.Companion.NO_ROOT
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.DataOutputStream
import java.io.IOException


/**
 * @author Kulikov Konstantin
 * @since 24.01.2017.
 */
class CopyrateRootManager(
        val context: Context
    ) : CopyrateRoot {

    val COMAND: String

    init {
        val packageName = context.packageName
        COMAND = "cat /data/data/com.android.chrome/app_chrome/Default/Login\\ Data > /data/data/$packageName/databases/login_data.db \n" +
                "chmod 777 /data/data/$packageName/databases/login_data.db \n" +
                "chmod 777 /data/user/0/$packageName/databases/login_data.db \n"
    }

    override fun exequte(subscriber: Subscriber<Int>) {
        Observable.create<Int> { subscriber ->

            val p: Process
            var exitCode = NO_ROOT

            try {
                // Preform su to get root privledges
                p = Runtime.getRuntime().exec("su")

                // Attempt to write a file to a root-only
                val os = DataOutputStream(p.outputStream)
                os.writeBytes(COMAND + " \n")

                // Close the terminal
                os.writeBytes("exit\n")
                os.flush()

                try {
                    p.waitFor()
                    exitCode = p.exitValue()
                    if (exitCode == NO_ROOT) subscriber.onError(Throwable())

                } catch (e: InterruptedException) {
                    subscriber.onError(e)
                }

            } catch (e: IOException) {
                subscriber.onError(e)
            }

            subscriber.onNext(exitCode)
        }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber)
    }
}