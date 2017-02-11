package ru.semper_viventem.chromeor.util

import android.app.Application
import com.google.android.gms.analytics.Tracker
import ru.semper_viventem.chromeor.di.component.AppComponent
import ru.semper_viventem.chromeor.di.module.AppModule

/**
 * This is a subclass of [Application] used to provide shared objects for this app, such as
 * the [Tracker].
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
                .build()
    }
}