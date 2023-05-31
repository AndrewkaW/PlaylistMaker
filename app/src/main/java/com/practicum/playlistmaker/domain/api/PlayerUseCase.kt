package com.practicum.playlistmaker.domain.api

interface PlayerUseCase {
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getPlayerState(): Int
    fun getCurrentTime(): Int
    fun getTrackName(): String
    fun getArtistName(): String
    fun getTrackTime(): Int
    fun getArtworkUrl(): String
    fun getCollectionName(): String
    fun isCollectionVisible(): Boolean
    fun getReleaseDate(): String
    fun getPrimaryGenre(): String
    fun getCountry(): String
}