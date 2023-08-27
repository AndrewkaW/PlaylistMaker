package com.practicum.playlistmaker.domain.favorites.impl

import com.practicum.playlistmaker.domain.favorites.FavoritesInteractor
import com.practicum.playlistmaker.domain.favorites.FavoritesRepository
import com.practicum.playlistmaker.domain.player.model.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(private val favoritesRepository: FavoritesRepository) : FavoritesInteractor {
    override suspend fun insertTrackToFavorites(track: Track){
        favoritesRepository.insertTrackToFavorites(track)
    }

    override suspend fun deleteTrackEntity(track: Track) {
        favoritesRepository.deleteTrackEntity(track)
    }

    override fun getFavoritesList(): Flow<List<Track>> = favoritesRepository.getFavoritesList()

    override fun getFavoritesIdList(): Flow<List<Int>> = favoritesRepository.getFavoritesIdList()
}