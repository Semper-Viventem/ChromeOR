package ru.semper_viventem.chromeor.util

import android.app.Application
import ru.semper_viventem.chromeor.di.component.AppComponent
import ru.semper_viventem.chromeor.di.component.DaggerAppComponent
import ru.semper_viventem.chromeor.di.module.AppModule
import ru.semper_viventem.chromeor.di.module.DataModule

/**
 * @author Kulikov Konstantin
 * @since 11.02.2017
 */
class App : Application() {

    companion object {
        val TAG = "ChromeOR"

        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent.builder()
                .appModule(AppModule(this, this))
                .dataModule(DataModule())
                .build()
    }
}