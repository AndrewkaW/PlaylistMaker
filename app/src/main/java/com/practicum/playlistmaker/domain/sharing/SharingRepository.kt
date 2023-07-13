package com.practicum.playlistmaker.domain.sharing

interface SharingRepository {
    fun shareLink()
    fun openLink()
    fun openEmail()
}
