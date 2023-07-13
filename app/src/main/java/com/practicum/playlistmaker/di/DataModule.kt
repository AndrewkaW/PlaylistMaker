package com.practicum.playlistmaker.di

import android.app.Application
import android.media.MediaPlayer
import com.practicum.playlistmaker.data.settings.ThemeStorage
import com.practicum.playlistmaker.data.settings.impl.ThemeStorageImpl
import com.practicum.playlistmaker.data.sharing.SharingStorage
import com.practicum.playlistmaker.data.sharing.impl.SharingRepositoryImpl
import com.practicum.playlistmaker.data.sharing.impl.SharingStorageImpl
import com.practicum.playlistmaker.domain.Constants
import com.practicum.playlistmaker.domain.sharing.SharingRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    single<SharingStorage> {
        SharingStorageImpl(get())
    }

    //Settings

    single {
        androidContext()
            .getSharedPreferences(
                Constants.APP_SETTINGS,
                Application.MODE_PRIVATE
            )
    }

    single <ThemeStorage>{
        ThemeStorageImpl(get(),get())
    }

}