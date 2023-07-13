package com.practicum.playlistmaker.domain.search.impl

import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.domain.search.SearchInteractor
import com.practicum.playlistmaker.domain.search.SearchRepository
import com.practicum.playlistmaker.utils.Resource
import java.util.concurrent.Executors

class SearchInteractorImpl(private val searchRepository: SearchRepository) : SearchInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTrackList(expression: String, consumer: SearchInteractor.SearchConsumer) {
        executor.execute {
            val resource = searchRepository.searchTrackList(expression)
            when (resource) {
                is Resource.Success -> {
                    consumer.consume(resource.data, null)
                }
                is Resource.Error -> {
                    consumer.consume(null, resource.message)
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