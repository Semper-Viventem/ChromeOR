package ru.semper_viventem.chromeor.data.repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ru.semper_viventem.chromeor.presentation.model.LoginEntity
import java.io.DataOutputStream

/**
 * @author Kulikov Konstantin
 * @since 09.04.2017.
 */
abstract class DataRepository(
        private val mContext: Context
) {

    /**
     * Путь до исходной базы данных [String]
     */
    abstract protected val ORIGIN_DATABASE_PATH: String

    /**
     * Название копии базы данных в пакете данного приложения [String]
     */
    abstract protected val NEW_DATABASE_NAME: String

    /**
     * Название колонки с адресом для авторизации [String]
     */
    abstract protected val ACTION_URL: String

    /**
     * Название колонки с адресом на страницу для авторизации [String]
     */
    abstract protected val ORIGIN_URL: String

    /**
     * Название колонки с именем пользователя [String]
     */
    abstract protected val USERNAME_VALUE: String

    /**
     * Название колонки с паролем пользователя [String]
     */
    abstract protected val PASSWORD_VALUE: String

    /**
     * Название таблицы с данными авторизации [String]
     */
    protected open val TABLE_NAME: String = "logins"



    protected val mSDB: SQLiteDatabase = DataRepository
            .RepositoryDBHelper(mContext, mContext.getDatabasePath(NEW_DATABASE_NAME).absolutePath)
            .readableDatabase

    /**
     * Копирование БД
     *
     * @return код результата итерации [Int]
     */
    protected open fun copyingDataBase(): Int {
        val COMMAND = "cat $ORIGIN_DATABASE_PATH > ${mContext.getDatabasePath(NEW_DATABASE_NAME).absolutePath}\n" +
                "chmod ugo+rwx ${mContext.getDatabasePath(NEW_DATABASE_NAME).absolutePath}\n"
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

    /**
     * Получить список пользовательских
     * аккаунтов из БД
     *
     * @return список [List] аккаунтов пользователя [LoginEntity]
     */
    protected open fun getLoginList(): List<LoginEntity> {
        val loginEntityList = ArrayList<LoginEntity>()

        val cursor = mSDB.query(TABLE_NAME, arrayOf(ACTION_URL, ORIGIN_URL, USERNAME_VALUE, PASSWORD_VALUE),
                null, null,
                null, null, null)

        cursor.moveToFirst()

        while (cursor.position < cursor.count) {
            val loginEntity = LoginEntity()

            loginEntity.actionUrl = cursor.getString(cursor.getColumnIndex(ACTION_URL))
            loginEntity.originUrl = cursor.getString(cursor.getColumnIndex(ORIGIN_URL))
            loginEntity.usernameValue = cursor.getString(cursor.getColumnIndex(USERNAME_VALUE))

            val blob = cursor.getBlob(cursor.getColumnIndex(PASSWORD_VALUE))
            loginEntity.passwordValue = String(blob)

            loginEntityList.add(loginEntity)

            cursor.moveToNext()
        }
        cursor.close()

        return loginEntityList
    }

    protected class RepositoryDBHelper(
            context: Context,
            name: String
    ) : SQLiteOpenHelper(context, name, null, 1) {

        override fun onCreate(db: SQLiteDatabase) {}
        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    }
}