package ru.goaliepash.domain.interactor

interface AppThemeInteractor {

    fun getAppTheme(): Boolean

    fun setAppTheme(darkThemeEnabled: Boolean)
}