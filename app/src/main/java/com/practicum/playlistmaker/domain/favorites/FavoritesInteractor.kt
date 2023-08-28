package com.practicum.playlistmaker.domain.favorites

import com.practicum.playlistmaker.domain.player.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    fun getFavoritesList(): Flow<List<Track>>
}