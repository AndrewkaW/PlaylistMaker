package com.practicum.playlistmaker.data.search.impl

import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.data.search.HistoryStorage
import com.practicum.playlistmaker.data.search.NetworkClient
import com.practicum.playlistmaker.data.search.network.model.TrackResponse
import com.practicum.playlistmaker.domain.search.SearchRepository
import com.practicum.playlistmaker.utils.Resource
import com.practicum.playlistmaker.utils.Resource.Companion.CONNECTION_ERROR
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val storage: HistoryStorage,
) : SearchRepository {

    override fun searchTrackList(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(expression)

        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error(CONNECTION_ERROR))
            }
            200 -> {
                val listResponse = response as TrackResponse
                if (listResponse.results.isEmpty() ) {
                    emit(Resource.Error(Resource.NOT_FOUND))
                } else {
                    val data = listResponse.results.map {
                        Track(trackId = it.trackId,
                            trackName = it.trackName,
                            artistName = it.artistName,
                            trackTimeMillis = it.trackTimeMillis,
                            artworkUrl100 = it.artworkUrl100,
                            collectionName = it.collectionName,
                            releaseDate = it.releaseDate,
                            primaryGenreName = it.primaryGenreName,
                            country = it.country,
                            previewUrl = it.previewUrl,)
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
}