package ru.goaliepash.playlistmaker.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.goaliepash.domain.interactor.AppThemeInteractor
import ru.goaliepash.playlistmaker.di.appModule
import ru.goaliepash.playlistmaker.di.dataModule
import ru.goaliepash.playlistmaker.di.domainModule

class App : Application() {

    private var darkTheme = false

    private val appThemeInteractor: AppThemeInteractor by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(appModule, domainModule, dataModule))
        }
        setTheme(appThemeInteractor.getAppTheme())
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        setTheme(darkThemeEnabled)
        saveDarkThemeStatus(darkThemeEnabled)
    }

    private fun setTheme(darkThemeEnabled: Boolean) {
        this.darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(if (darkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun saveDarkThemeStatus(darkThemeEnabled: Boolean) = appThemeInteractor.setAppTheme(darkThemeEnabled)
}