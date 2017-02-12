package ru.semper_viventem.chromeor.data.repository.chrome

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import ru.semper_viventem.chromeor.data.repository.WorkDB
import ru.semper_viventem.chromeor.presentation.model.LoginEntity
import rx.Observable
import java.io.DataOutputStream
import java.io.File
import java.io.IOException
import java.util.*
import javax.inject.Inject

/**
 * @author Kulikov Konstantin
 * @since 12.02.2017.
 */
class ChromeDataRepository @Inject constructor(
        private val mContext: Context
): ChromeDataStore {

    override fun copyData(): Observable<Int> {
        val packageName = mContext.packageName
        val COMAND = "cat /data/data/com.android.chrome/app_chrome/Default/Login\\ Data > /data/data/$packageName/databases/login_data.db \n" +
                "chmod 777 /data/data/$packageName/databases/login_data.db \n" +
                "chmod 777 /data/user/0/$packageName/databases/login_data.db \n"
        val NOT_ROOT = 255

        return Observable.create<Int> { subscriber ->

            val p: Process
            var exitCode = NOT_ROOT

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
                    if (exitCode == NOT_ROOT) subscriber.onError(Throwable())

                } catch (e: InterruptedException) {
                    subscriber.onError(e)
                }

            } catch (e: IOException) {
                subscriber.onError(e)
            }

            subscriber.onNext(exitCode)
        }
    }


    override fun getData(): Observable<List<LoginEntity>> {
        val PACKAGE_NAME = mContext.packageName
        val DB_DESTINATION = "/data/data/$PACKAGE_NAME/databases/"
        val DB_NAME = "login_data.db"
        val DB_BASE_DEST = DB_DESTINATION + File.separator + DB_NAME
        var mDbFile: File = File(DB_BASE_DEST)

        return Observable.create<List<LoginEntity>> { subscriber ->
            val loginEntityList = ArrayList<LoginEntity>()

            try {
                val sdb = SQLiteDatabase.openDatabase(mDbFile.path, null, 0)

                val cursor = sdb.query("logins", arrayOf(WorkDB.action_url, WorkDB.origin_url, WorkDB.username_value, WorkDB.password_value),
                        null, null,
                        null, null, null)

                cursor.moveToFirst()

                while (cursor.moveToNext()) {
                    val loginEntity = LoginEntity()

                    loginEntity.actionUrl = cursor.getString(cursor.getColumnIndex(WorkDB.action_url))
                    loginEntity.originUrl = cursor.getString(cursor.getColumnIndex(WorkDB.origin_url))
                    loginEntity.usernameValue = cursor.getString(cursor.getColumnIndex(WorkDB.username_value))

                    val blob = cursor.getBlob(cursor.getColumnIndex(WorkDB.password_value))
                    loginEntity.passwordValue = String(blob)

                    loginEntityList.add(loginEntity)
                }
                cursor.close()
            } catch (e: Exception) {
                e.printStackTrace()
                subscriber.onError(e)
            }

            subscriber.onNext(loginEntityList)
        }
    }
}