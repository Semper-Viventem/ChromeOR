package ru.semper_viventem.chromeor.di.component

import dagger.Component
import ru.semper_viventem.chromeor.di.module.AppModule
import ru.semper_viventem.chromeor.presentation.presenter.AboutPresenter
import ru.semper_viventem.chromeor.presentation.presenter.MainPresenter
import javax.inject.Singleton

/**
 * @author Kulikov Konstantin
 * @since 11.02.2017.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {

    fun inject(mainPresenter: MainPresenter)
    fun inject(aboutPresenter: AboutPresenter)
}