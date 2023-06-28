package com.practicum.playlistmaker

import android.app.Application
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.settings.SettingsInteractor

class App : Application() {

    lateinit var settingsInteractor: SettingsInteractor

    override fun onCreate() {
        super.onCreate()
        settingsInteractor = Creator.provideSettingsInteractor(this)
        settingsInteractor.applyCurrentTheme()
    }

}