package com.practicum.playlistmaker.data.search.impl

import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.data.search.HistoryStorage
import com.practicum.playlistmaker.data.search.NetworkClient
import com.practicum.playlistmaker.data.search.network.model.TrackDto
import com.practicum.playlistmaker.data.search.network.model.TrackResponse
import com.practicum.playlistmaker.domain.search.SearchRepository
import com.practicum.playlistmaker.utils.Resource
import com.practicum.playlistmaker.utils.Resource.Companion.CONNECTION_ERROR
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val storage: HistoryStorage,
    private val appDatabase: AppDatabase
) : SearchRepository {

    override fun searchTrackList(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(expression)

        when (response.resultCode) {
            ERROR_CODE -> {
                emit(Resource.Error(CONNECTION_ERROR))
            }

            SUCCESS_CODE -> {
                val listResponse = response as TrackResponse
                if (listResponse.results.isEmpty()) {
                    emit(Resource.Error(Resource.NOT_FOUND))
                } else {
                    val data = listResponse.results.map {
                        trackDtoToTrack(it)
                    }
                    emit(Resource.Success(data))
                }
            }

            else -> {
                emit(Resource.Error(CONNECTION_ERROR))
            }
        }
    }

    override fun clearHistoryList() {
        storage.clearList()
    }

    override fun getHistoryList(): ArrayList<Track> {
        return storage.getList()
    }

    override fun addTrackToHistory(track: Track) {
        storage.addTrack(track)
    }

    private suspend fun trackIsFavorite(id: Int): Boolean {
        return appDatabase.favoritesDao().getFavoritesIdList().contains(id)
    }

    private suspend fun trackDtoToTrack(trackDto: TrackDto): Track {
        return Track(
            trackId = trackDto.trackId,
            trackName = trackDto.trackName,
            artistName = trackDto.artistName ?: "",
            trackTimeMillis = trackDto.trackTimeMillis ?: 0,
            artworkUrl100 = trackDto.artworkUrl100 ?: "",
            artworkUrl60 = trackDto.artworkUrl60 ?: "",
            collectionName = trackDto.collectionName ?: "",
            releaseDate = trackDto.releaseDate ?: "",
            primaryGenreName = trackDto.primaryGenreName ?: "",
            country = trackDto.country ?: "",
            previewUrl = trackDto.previewUrl ?: "",
            isFavorite = trackIsFavorite(trackDto.trackId)
        )
    }

    companion object {
        const val ERROR_CODE = -1
        const val SUCCESS_CODE = 200
    }
}