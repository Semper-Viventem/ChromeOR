package ru.semper_viventem.chromeor.data.repository.chrome_beta

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import ru.semper_viventem.chromeor.data.repository.chrome_beta.ChromeBetaDataStore.Companion.DB_NAME
import ru.semper_viventem.chromeor.data.repository.chrome_beta.ChromeBetaDataStore.Companion.DB_PACKAGE
import ru.semper_viventem.chromeor.data.repository.chrome_beta.ChromeBetaDataStore.Companion.action_url
import ru.semper_viventem.chromeor.data.repository.chrome_beta.ChromeBetaDataStore.Companion.origin_url
import ru.semper_viventem.chromeor.data.repository.chrome_beta.ChromeBetaDataStore.Companion.password_value
import ru.semper_viventem.chromeor.data.repository.chrome_beta.ChromeBetaDataStore.Companion.username_value
import ru.semper_viventem.chromeor.presentation.model.LoginEntity
import rx.Observable
import java.io.DataOutputStream
import java.io.File
import java.io.IOException
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Kulikov Konstantin
 * @since 18.02.2017.
 */
@Singleton
class ChromeBetaDataRepository @Inject constructor(
        private val mContext: Context
): ChromeBetaDataStore {

    override fun copyData(): Observable<Int> {
        val COMMAND = "cat $DB_PACKAGE > ${mContext.applicationInfo.dataDir}/databases/$DB_NAME \n"
        val NOT_ROOT = 255

        return Observable.create<Int> { subscriber ->

            val p: Process
            var exitCode = NOT_ROOT

            try {
                // Preform su to get root privledges
                p = Runtime.getRuntime().exec("su")

                // Attempt to write a file to a root-only
                val os = DataOutputStream(p.outputStream)
                os.writeBytes(COMMAND + " \n")

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
        val DB_DESTINATION = mContext.applicationInfo.dataDir + "/databases/"
        val DB_BASE_DEST = DB_DESTINATION + File.separator + DB_NAME
        val mDbFile: File = File(DB_BASE_DEST)

        return Observable.create<List<LoginEntity>> { subscriber ->
            val loginEntityList = ArrayList<LoginEntity>()

            try {
                val sdb = SQLiteDatabase.openDatabase(mDbFile.path, null, 0)

                val cursor = sdb.query("logins", arrayOf(action_url, origin_url, username_value, password_value),
                        null, null,
                        null, null, null)

                cursor.moveToFirst()

                while (cursor.position < cursor.count) {
                    val loginEntity = LoginEntity()

                    loginEntity.actionUrl = cursor.getString(cursor.getColumnIndex(action_url))
                    loginEntity.originUrl = cursor.getString(cursor.getColumnIndex(origin_url))
                    loginEntity.usernameValue = cursor.getString(cursor.getColumnIndex(username_value))

                    val blob = cursor.getBlob(cursor.getColumnIndex(password_value))
                    loginEntity.passwordValue = String(blob)

                    loginEntityList.add(loginEntity)

                    cursor.moveToNext()
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