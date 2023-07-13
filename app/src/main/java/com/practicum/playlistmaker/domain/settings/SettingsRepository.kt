package com.practicum.playlistmaker.domain.settings


interface SettingsRepository {
    fun themeIsDark(): Boolean
    fun updateThemeSetting(isDark : Boolean)
    fun applyCurrentTheme()
}