package com.practicum.playlistmaker.di

import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.search.HistoryStorage
import com.practicum.playlistmaker.data.search.NetworkClient
import com.practicum.playlistmaker.data.search.impl.HistoryStorageImpl
import com.practicum.playlistmaker.data.search.network.ItunesApi
import com.practicum.playlistmaker.data.search.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.settings.ThemeStorage
import com.practicum.playlistmaker.data.settings.impl.ThemeStorageImpl
import com.practicum.playlistmaker.data.sharing.SharingStorage
import com.practicum.playlistmaker.data.sharing.impl.SharingStorageImpl
import com.practicum.playlistmaker.domain.APP_SETTINGS
import com.practicum.playlistmaker.domain.ITUNES_URL
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<SharingStorage> {
        SharingStorageImpl(get())
    }

    single {
        androidContext()
            .getSharedPreferences(
                APP_SETTINGS,
                Application.MODE_PRIVATE
            )
    }

    single<ThemeStorage> {
        ThemeStorageImpl(get(), get())
    }

    single<ItunesApi> {

        Retrofit.Builder()
            .baseUrl(ITUNES_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesApi::class.java)
    }

    single {
        Gson()
    }

    single<HistoryStorage> {
        HistoryStorageImpl(get(), get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), get())
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}