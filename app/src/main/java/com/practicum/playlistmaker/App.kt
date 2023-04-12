package com.practicum.playlistmaker

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate

const val APP_SETTINGS = "APP_SETTINGS"
const val DARK_THEME = "DARK_THEME"

class App: Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        val sharedPrefs = getSharedPreferences(APP_SETTINGS, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(DARK_THEME, isDarkThemeOn())
        switchTheme(darkTheme)

    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        if (darkThemeEnabled!=darkTheme) {
            darkTheme = darkThemeEnabled
            val sharedPrefs = getSharedPreferences(APP_SETTINGS, MODE_PRIVATE)
            sharedPrefs.edit()
                .putBoolean(DARK_THEME,darkTheme)
                .apply()
        }
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
    private fun Context.isDarkThemeOn() = resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES

}