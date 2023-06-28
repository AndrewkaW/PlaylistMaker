package com.practicum.playlistmaker.ui.settings.view_model

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.domain.sharing.SharingInteractor

class SettingsViewModel(val app: App, val sharingInteractor: SharingInteractor) :
    AndroidViewModel(app) {

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
        app.settingsInteractor.updateThemeSetting(isDark)
    }

    fun isCheckedTheme(): Boolean {
        return app.settingsInteractor.getThemeSettings()
    }

    companion object {
        fun getViewModelFactory(
            app: App,
            sharingInteractor: SharingInteractor
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SettingsViewModel(
                        app = app,
                        sharingInteractor = sharingInteractor

                    ) as T
                }
            }
    }
}