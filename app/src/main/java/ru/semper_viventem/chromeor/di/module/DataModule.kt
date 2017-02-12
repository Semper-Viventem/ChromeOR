package ru.semper_viventem.chromeor.di.module

import dagger.Module
import dagger.Provides
import ru.semper_viventem.chromeor.data.repository.chrome.ChromeDataRepository
import ru.semper_viventem.chromeor.data.repository.chrome.ChromeDataStore
import javax.inject.Singleton

/**
 * @author Kulikov Konstantin
 * @since 12.02.2017.
 */
@Module
class DataModule {

    @Provides
    @Singleton
    fun provideChromeDataStore(chromeDataRepository: ChromeDataRepository): ChromeDataStore = chromeDataRepository
}