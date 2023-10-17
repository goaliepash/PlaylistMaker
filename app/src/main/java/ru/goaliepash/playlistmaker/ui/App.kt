package ru.goaliepash.playlistmaker.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import ru.goaliepash.playlistmaker.util.Creator

class App : Application() {

    private var darkTheme = false

    private val appThemeInteractor by lazy { Creator.provideAppThemeInteractor(applicationContext) }

    override fun onCreate() {
        super.onCreate()
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