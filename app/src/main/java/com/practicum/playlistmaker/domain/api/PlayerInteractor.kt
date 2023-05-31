package com.practicum.playlistmaker.domain.api

interface PlayerInteractor {
    fun setPlayButtonEnabled(isEnabled: Boolean)
    fun setPlayButtonImage(resId: Int)
    fun setPlayTimeText(time: String)
}