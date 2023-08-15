package com.practicum.playlistmaker.domain.search.impl

import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.domain.search.SearchInteractor
import com.practicum.playlistmaker.domain.search.SearchRepository
import com.practicum.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchInteractorImpl(private val searchRepository: SearchRepository) : SearchInteractor {

    override fun searchTrackList(expression: String) : Flow<Pair<List<Track>?, String?>> {
        return searchRepository.searchTrackList(expression).map { result ->
            when(result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

    override fun clearHistoryList() {
        searchRepository.clearHistoryList()
    }

    override fun getHistoryList(): ArrayList<Track> {
        return searchRepository.getHistoryList()
    }

    override fun addTrackToHistory(track: Track) {
        searchRepository.addTrackToHistory(track)
    }
}