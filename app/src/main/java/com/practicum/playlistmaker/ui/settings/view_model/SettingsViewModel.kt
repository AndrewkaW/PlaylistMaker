package com.practicum.playlistmaker.ui.settings.view_model

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.sharing.SharingInteractor

class SettingsViewModel(
    //val app: App,
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor) :
   // AndroidViewModel(app) {
 ViewModel() {

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

    fun switchTheme(isDark: Boolean) {
        settingsInteractor.updateThemeSetting(isDark)
    }

    fun isCheckedTheme(): Boolean {
        return settingsInteractor.getThemeSettings()
    }
}