package ru.semper_viventem.chromeor.data.repository.chrome

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ru.semper_viventem.chromeor.domain.store.ChromeDataStore
import ru.semper_viventem.chromeor.domain.store.ChromeDataStore.Companion.DB_NAME
import ru.semper_viventem.chromeor.domain.store.ChromeDataStore.Companion.DB_PACKAGE
import ru.semper_viventem.chromeor.domain.store.ChromeDataStore.Companion.action_url
import ru.semper_viventem.chromeor.domain.store.ChromeDataStore.Companion.origin_url
import ru.semper_viventem.chromeor.domain.store.ChromeDataStore.Companion.password_value
import ru.semper_viventem.chromeor.domain.store.ChromeDataStore.Companion.username_value
import ru.semper_viventem.chromeor.presentation.model.LoginEntity
import java.io.DataOutputStream
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

    private var mSDB: SQLiteDatabase

    init {
        mSDB = WorkDB(mContext, mContext.getDatabasePath(ChromeDataStore.DB_NAME).absolutePath).readableDatabase
    }

    override fun copyData(): Int {
        val COMMAND = "cat $DB_PACKAGE > ${mContext.getDatabasePath(DB_NAME).absolutePath}\n" +
                "chmod ugo+rwx ${mContext.getDatabasePath(DB_NAME).absolutePath}\n"
        val NOT_ROOT = 255

        val p: Process = Runtime.getRuntime().exec("su")
        var exitCode = NOT_ROOT

        val os = DataOutputStream(p.outputStream)
        os.writeBytes(COMMAND + " \n")

        // Close the terminal
        os.writeBytes("exit\n")
        os.flush()

        p.waitFor()
        exitCode = p.exitValue()

        return exitCode
    }


    override fun getData(): List<LoginEntity> {
        val loginEntityList = ArrayList<LoginEntity>()

        val cursor = mSDB.query("logins", arrayOf(action_url, origin_url, username_value, password_value),
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

        return loginEntityList
    }


    /**
     * @author Kulikov Konstantin
     * @since 09.04.2017.
     */
    class WorkDB(
            context: Context,
            name: String
    ) : SQLiteOpenHelper(context, name, null, 1) {

        override fun onCreate(db: SQLiteDatabase) {}
        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    }
}