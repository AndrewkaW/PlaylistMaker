package com.practicum.playlistmaker.data.favorites

import com.practicum.playlistmaker.data.TrackDbConvertor
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.db.entity.TrackEntity
import com.practicum.playlistmaker.domain.favorites.FavoritesRepository
import com.practicum.playlistmaker.domain.player.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
) : FavoritesRepository {

    override fun getFavoritesList(): Flow<List<Track>> {
        return flow {
            val tracks = appDatabase
                .favoritesDao()
                .getFavoritesList()
            emit(convertFromTrackEntity(tracks))
        }
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }

}