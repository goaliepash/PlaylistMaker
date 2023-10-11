package ru.goaliepash.playlistmaker.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import ru.goaliepash.playlistmaker.util.Creator

class App : Application() {

    var darkTheme = false

    private val getAppThemeUseCase by lazy { Creator.provideGetAppThemeUseCase(applicationContext) }
    private val setAppThemeUseCase by lazy { Creator.provideSetAppThemeUseCase(applicationContext) }

    override fun onCreate() {
        super.onCreate()
        setTheme(getAppThemeUseCase())
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        setTheme(darkThemeEnabled)
        saveDarkThemeStatus(darkThemeEnabled)
    }

    private fun setTheme(darkThemeEnabled: Boolean) {
        this.darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    private fun saveDarkThemeStatus(darkThemeEnabled: Boolean) = setAppThemeUseCase(darkThemeEnabled)
}