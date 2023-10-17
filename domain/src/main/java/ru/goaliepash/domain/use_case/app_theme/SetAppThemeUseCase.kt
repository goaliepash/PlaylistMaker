package ru.goaliepash.domain.use_case.app_theme

import ru.goaliepash.domain.api.AppThemeRepository

class SetAppThemeUseCase(private val appThemeRepository: AppThemeRepository) {

    operator fun invoke(darkThemeEnabled: Boolean) {
        appThemeRepository.setDarkThemeEnabled(darkThemeEnabled)
    }
}