package ru.goaliepash.domain.interactor.implementation

import ru.goaliepash.domain.api.AppThemeRepository
import ru.goaliepash.domain.interactor.AppThemeInteractor

class AppThemeInteractorImpl(private val appThemeRepository: AppThemeRepository) : AppThemeInteractor {

    override fun getAppTheme(): Boolean {
        return appThemeRepository.isDarkThemeEnabled()
    }

    override fun setAppTheme(darkThemeEnabled: Boolean) {
        appThemeRepository.setDarkThemeEnabled(darkThemeEnabled)
    }
}