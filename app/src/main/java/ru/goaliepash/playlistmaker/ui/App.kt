package ru.goaliepash.playlistmaker.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import ru.goaliepash.playlistmaker.data.repository.AppThemeRepositoryImpl
import ru.goaliepash.playlistmaker.data.shared_preferences.app_theme.AppThemeClientImpl
import ru.goaliepash.playlistmaker.domain.use_case.app_theme.GetAppThemeUseCase
import ru.goaliepash.playlistmaker.domain.use_case.app_theme.SetAppThemeUseCase

class App : Application() {

    var darkTheme = false

    private val appThemeClient by lazy { AppThemeClientImpl(applicationContext) }
    private val appThemeRepository by lazy { AppThemeRepositoryImpl(appThemeClient) }
    private val getAppThemeUseCase by lazy { GetAppThemeUseCase(appThemeRepository) }
    private val setAppThemeUseCase by lazy { SetAppThemeUseCase(appThemeRepository) }

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