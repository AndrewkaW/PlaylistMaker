package com.practicum.playlistmaker.data.sharing

import com.practicum.playlistmaker.domain.sharing.model.EmailData

interface SharingStorage {
    fun getShareAppLink(): String
    fun getTermsLink(): String
    fun getSupportEmailData(): EmailData
}