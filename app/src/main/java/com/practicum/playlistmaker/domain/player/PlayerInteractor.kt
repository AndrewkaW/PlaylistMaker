package com.practicum.playlistmaker.domain.player

interface PlayerInteractor {
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getPlayerState(): Int
    fun getCurrentTime(): Int
}