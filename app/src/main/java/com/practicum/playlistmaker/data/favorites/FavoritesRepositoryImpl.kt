package com.practicum.playlistmaker.data.favorites

import com.practicum.playlistmaker.data.TrackDbConvertor
import com.practicum.playlistmaker.data.favorites.db.AppDatabase
import com.practicum.playlistmaker.data.favorites.db.entity.TrackEntity
import com.practicum.playlistmaker.domain.favorites.FavoritesRepository
import com.practicum.playlistmaker.domain.player.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
) : FavoritesRepository {
    override suspend fun insertTrackToFavorites(track: Track) {
        appDatabase
            .favoritesDao()
            .insertTrackEntity(convertToTrackEntity(track))
    }

    override suspend fun deleteTrackEntity(track: Track) {
        appDatabase
            .favoritesDao()
            .deleteTrackEntity(convertToTrackEntity(track))
    }

    override fun getFavoritesList(): Flow<List<Track>> = flow {
        val tracks = appDatabase
            .favoritesDao()
            .getFavoritesList()
        emit(convertFromTrackEntity(tracks))
    }

    override fun getFavoritesIdList(): Flow<List<Int>> = flow {
        val id = appDatabase
            .favoritesDao()
            .getFavoritesIdList()
        emit(id)
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { tracks -> trackDbConvertor.map(tracks) }
    }

    private fun convertToTrackEntity(track : Track) : TrackEntity {
        return trackDbConvertor.map(track)
    }
}