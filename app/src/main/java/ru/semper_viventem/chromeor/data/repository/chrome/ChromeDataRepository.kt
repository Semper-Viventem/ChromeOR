package ru.semper_viventem.chromeor.data.repository.chrome

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import ru.semper_viventem.chromeor.domain.store.ChromeDataStore
import ru.semper_viventem.chromeor.domain.store.ChromeDataStore.Companion.DB_NAME
import ru.semper_viventem.chromeor.domain.store.ChromeDataStore.Companion.DB_PACKAGE
import ru.semper_viventem.chromeor.domain.store.ChromeDataStore.Companion.action_url
import ru.semper_viventem.chromeor.domain.store.ChromeDataStore.Companion.origin_url
import ru.semper_viventem.chromeor.domain.store.ChromeDataStore.Companion.password_value
import ru.semper_viventem.chromeor.domain.store.ChromeDataStore.Companion.username_value
import ru.semper_viventem.chromeor.presentation.model.LoginEntity
import java.io.DataOutputStream
import java.io.File
import java.io.IOException
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Kulikov Konstantin
 * @since 12.02.2017.
 */
@Singleton
class ChromeDataRepository @Inject constructor(
        private val mContext: Context
): ChromeDataStore {

    override fun copyData(): Int {
        val COMMAND = "cat $DB_PACKAGE > ${mContext.applicationInfo.dataDir}/databases/$DB_NAME \n"
        val NOT_ROOT = 255

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

            } catch (e: InterruptedException) {
                e.printStackTrace()
                //TODO
            }

        } catch (e: IOException) {
            e.printStackTrace()
            //TODO
        }

        return exitCode
    }


    override fun getData(): List<LoginEntity> {
        val DB_DESTINATION = mContext.applicationInfo.dataDir + "/databases/"
        val DB_BASE_DEST = DB_DESTINATION + File.separator + DB_NAME
        val mDbFile: File = File(DB_BASE_DEST)

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
            //TODO
        }

        return loginEntityList
    }
}