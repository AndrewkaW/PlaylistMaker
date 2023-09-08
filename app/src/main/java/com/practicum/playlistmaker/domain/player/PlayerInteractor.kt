package com.practicum.playlistmaker.domain.player

import com.practicum.playlistmaker.domain.player.model.Track
import kotlinx.coroutines.flow.Flow

interface PlayerInteractor {
    fun prepareTrack(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getPlayerState(): Int
    fun getCurrentTime(): Int
    fun trackIsFavorite(track: Track): Flow<Boolean>
    suspend fun deleteFavoriteTrack(track: Track)
    suspend fun addTrackToFavorites(track: Track)
}