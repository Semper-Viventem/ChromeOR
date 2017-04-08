package ru.semper_viventem.chromeor.di.module

import dagger.Module
import dagger.Provides
import ru.semper_viventem.chromeor.data.repository.chrome.ChromeDataRepository
import ru.semper_viventem.chromeor.data.repository.chrome_beta.ChromeBetaDataRepository
import ru.semper_viventem.chromeor.data.repository.yandex.YandexDataRepository
import ru.semper_viventem.chromeor.domain.store.ChromeBetaDataStore
import ru.semper_viventem.chromeor.domain.store.ChromeDataStore
import ru.semper_viventem.chromeor.domain.store.YandexDataStore
import javax.inject.Singleton

/**
 * @author Kulikov Konstantin
 * @since 12.02.2017.
 */
@Module
class DataModule {

    @Provides
    @Singleton
    fun provideChromeDataStore(repository: ChromeDataRepository): ChromeDataStore = repository

    @Provides
    @Singleton
    fun provideYandexDataStore(repository: YandexDataRepository): YandexDataStore = repository

    @Provides
    @Singleton
    fun provideChromeBetaDataStore(repository: ChromeBetaDataRepository): ChromeBetaDataStore = repository
}