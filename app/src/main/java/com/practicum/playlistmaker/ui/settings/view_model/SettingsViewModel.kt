package com.practicum.playlistmaker.ui.settings.view_model

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.sharing.SharingInteractor

class SettingsViewModel(app: App ,val sharingInteractor: SharingInteractor) : AndroidViewModel(app) {


//    private val settingsInteractor: SettingsInteractor

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms(){
        sharingInteractor.openTerms()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

    companion object {
        fun getViewModelFactory(app: App,sharingInteractor: SharingInteractor): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SettingsViewModel(
                        app = app,
                        sharingInteractor = sharingInteractor

                    ) as T
                }
            }
    }
}