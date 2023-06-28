package com.practicum.playlistmaker.data.settings

interface ThemeStorage {
    fun updateThemeSetting(darkTheme: Boolean)
    fun themeIsDark(): Boolean
}