package ru.goaliepash.playlistmaker.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.goaliepash.domain.interactor.AppThemeInteractor

class SettingsViewModel(private val appThemeInteractor: AppThemeInteractor) : ViewModel() {

    private val isThemeDark = MutableLiveData<Boolean>()

    fun isThemeDark(): LiveData<Boolean> = isThemeDark

    fun getAppTheme() {
        isThemeDark.value = appThemeInteractor.getAppTheme()
    }
}