package com.practicum.playlistmaker.domain.player

interface PlayerInteractor {
    fun prepareTrack(url:String)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getPlayerState(): Int
    fun getCurrentTime(): Int
}