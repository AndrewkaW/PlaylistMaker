package com.practicum.playlistmaker.domain.player

import com.practicum.playlistmaker.domain.player.model.Track
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    fun prepareTrack(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getPlayerState(): Int
    fun getCurrentTime(): Int
    fun getFavoritesIdList(): Flow<List<Int>>
    suspend fun deleteTrackEntity(track: Track)
    suspend fun insertTrackToFavorites(track: Track)
}