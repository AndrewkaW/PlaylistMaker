package com.practicum.playlistmaker.domain.settings.impl

import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.SettingsRepository

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository) :
    SettingsInteractor {
    override fun getThemeSettings(): Boolean {
        return settingsRepository.themeIsDark()
    }

    override fun updateThemeSetting(isDark: Boolean) {
        return settingsRepository.updateThemeSetting(isDark)
    }

    override fun applyCurrentTheme() {
        return settingsRepository.applyCurrentTheme()
    }
}