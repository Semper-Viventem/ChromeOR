package ru.semper_viventem.chromeor.domain

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import ru.semper_viventem.chromeor.data.WorkDB
import ru.semper_viventem.chromeor.presentation.model.LoginEntity
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File
import java.util.*


/**
 * @author Kulikov Konstantin
 * @since 24.01.2017.
 */
class GetLoginFromDB(
        val context: Context
    ) {

    var mDbFile: File

    init {
        val PACKAGE_NAME = context.packageName
        val DB_DESTINATION = "/data/data/$PACKAGE_NAME/databases/"
        val DB_NAME = "login_data.db"
        val DB_BASE_DEST = DB_DESTINATION + File.separator + DB_NAME
        mDbFile = File(DB_BASE_DEST)
    }

    fun execute(subscriber: Subscriber<List<LoginEntity>>) {
        Observable.create<List<LoginEntity>> { subscriber ->
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
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber)
    }
}