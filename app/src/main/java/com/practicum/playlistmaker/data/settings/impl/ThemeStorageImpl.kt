package com.practicum.playlistmaker.data.settings.impl

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import com.practicum.playlistmaker.data.settings.ThemeStorage
import com.practicum.playlistmaker.domain.DARK_THEME


class ThemeStorageImpl(
    private val sharedPreferences: SharedPreferences,
    private val context: Context
) : ThemeStorage {

    override fun updateThemeSetting(darkTheme: Boolean) {
        sharedPreferences
            .edit()
            .putBoolean(DARK_THEME, darkTheme)
            .apply()
    }

    override fun themeIsDark(): Boolean {
        return sharedPreferences.getBoolean(DARK_THEME, systemThemeIsDark())
    }

    private fun systemThemeIsDark(): Boolean {
        val nightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightMode == Configuration.UI_MODE_NIGHT_YES
    }

}
