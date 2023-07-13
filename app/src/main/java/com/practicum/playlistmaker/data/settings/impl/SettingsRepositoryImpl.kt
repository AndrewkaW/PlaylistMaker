package com.practicum.playlistmaker.data.settings.impl

import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.data.settings.ThemeStorage
import com.practicum.playlistmaker.domain.settings.SettingsRepository


class SettingsRepositoryImpl(private val themeStorage: ThemeStorage) : SettingsRepository {

    override fun themeIsDark(): Boolean {
        return themeStorage.themeIsDark()
    }

    override fun updateThemeSetting(isDark: Boolean) {
        themeStorage.updateThemeSetting(isDark)
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    override fun applyCurrentTheme() {
        updateThemeSetting(themeIsDark())

    }
}