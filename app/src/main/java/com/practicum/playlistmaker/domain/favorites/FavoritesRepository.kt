package com.practicum.playlistmaker.domain.favorites

import com.practicum.playlistmaker.data.favorites.db.entity.TrackEntity
import com.practicum.playlistmaker.domain.player.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    suspend fun insertTrackToFavorites(track: Track)
    suspend fun deleteTrackEntity(track: Track)
    fun getFavoritesList(): Flow<List<Track>>
    fun getFavoritesIdList(): Flow<List<Int>>
}