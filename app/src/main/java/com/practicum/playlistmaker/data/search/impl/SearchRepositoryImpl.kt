package com.practicum.playlistmaker.data.search.impl

import com.practicum.playlistmaker.Track
import com.practicum.playlistmaker.data.search.HistoryStorage
import com.practicum.playlistmaker.data.search.NetworkClient
import com.practicum.playlistmaker.data.search.network.model.TrackResponse
import com.practicum.playlistmaker.domain.search.SearchRepository
import com.practicum.playlistmaker.utils.Resource
import com.practicum.playlistmaker.utils.Resource.Companion.CONNECTION_ERROR

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val storage: HistoryStorage,
) : SearchRepository {

    override fun searchTrackList(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(expression)
        return if (response.resultCode == 200) {
            val listResponse = response as TrackResponse
            if (listResponse.resultCount != 0) {
                Resource.Success(data = listResponse.results)
            } else {
                Resource.Error(Resource.NOT_FOUND)
            }
        } else {
            Resource.Error(CONNECTION_ERROR)
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