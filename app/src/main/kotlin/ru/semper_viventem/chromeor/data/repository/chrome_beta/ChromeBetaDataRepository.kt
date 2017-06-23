package ru.semper_viventem.chromeor.data.repository.chrome_beta

import android.content.Context
import ru.semper_viventem.chromeor.data.repository.DataRepository
import ru.semper_viventem.chromeor.domain.store.ChromeBetaDataStore
import ru.semper_viventem.chromeor.domain.store.ChromeBetaDataStore.Companion.DB_NAME
import ru.semper_viventem.chromeor.domain.store.ChromeBetaDataStore.Companion.DB_PACKAGE
import ru.semper_viventem.chromeor.domain.store.ChromeBetaDataStore.Companion.action_url
import ru.semper_viventem.chromeor.domain.store.ChromeBetaDataStore.Companion.origin_url
import ru.semper_viventem.chromeor.domain.store.ChromeBetaDataStore.Companion.password_value
import ru.semper_viventem.chromeor.domain.store.ChromeBetaDataStore.Companion.username_value
import ru.semper_viventem.chromeor.presentation.model.LoginEntity
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Kulikov Konstantin
 * @since 18.02.2017.
 */
@Singleton
class ChromeBetaDataRepository @Inject constructor(
        context: Context
): DataRepository(context), ChromeBetaDataStore {


    override val NEW_DATABASE_NAME: String
        get() = DB_NAME

    override val ORIGIN_DATABASE_PATH: String
        get() = DB_PACKAGE

    override val ACTION_URL: String
        get() = action_url

    override val ORIGIN_URL: String
        get() = origin_url

    override val USERNAME_VALUE: String
        get() = username_value

    override val PASSWORD_VALUE: String
        get() = password_value

    override fun copyData(): Int {
        return copyingDataBase()
    }

    override fun getData(): List<LoginEntity> {
        return getLoginList()
    }

}