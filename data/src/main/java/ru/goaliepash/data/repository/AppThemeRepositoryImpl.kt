package ru.goaliepash.data.repository

import ru.goaliepash.data.shared_preferences.app_theme.AppThemeClient
import ru.goaliepash.domain.api.AppThemeRepository

class AppThemeRepositoryImpl(private val appThemeClient: AppThemeClient) : AppThemeRepository {

    override fun isDarkThemeEnabled(): Boolean {
        return appThemeClient.get()
    }

    override fun setDarkThemeEnabled(darkThemeEnabled: Boolean) {
        appThemeClient.set(darkThemeEnabled)
    }
}