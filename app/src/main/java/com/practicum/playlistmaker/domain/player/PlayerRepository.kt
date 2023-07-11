package com.practicum.playlistmaker.domain.player

interface PlayerRepository {
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getPlayerState(): Int
    fun getCurrentTime(): Int
}