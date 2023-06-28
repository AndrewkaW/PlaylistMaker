package com.practicum.playlistmaker.domain.settings


interface SettingsInteractor {
    fun getThemeSettings(): Boolean
    fun updateThemeSetting(isDark: Boolean)
    fun applyCurrentTheme()
}