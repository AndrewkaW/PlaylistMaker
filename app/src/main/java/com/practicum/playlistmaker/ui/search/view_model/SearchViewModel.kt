package com.practicum.playlistmaker.ui.search.view_model

import androidx.lifecycle.*
import com.practicum.playlistmaker.domain.CLICK_ITEM_DELAY
import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.domain.search.SearchInteractor
import com.practicum.playlistmaker.ui.search.view_model.model.SearchState
import com.practicum.playlistmaker.utils.Resource
import com.practicum.playlistmaker.utils.debounce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {

    private var isClickAllowed = true

    private var lastSearchText = ""

    private val tracksSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { searchText ->
            searchTrackList(searchText)
        }

    private val _stateLiveData = MutableLiveData<SearchState>()
    val stateLiveData: LiveData<SearchState> get() = _stateLiveData

    init {
        getHistoryList()
    }

    fun searchTrackList(query: String) {
        if (query.isNotEmpty()) {
            _stateLiveData.postValue(SearchState.Loading)

            viewModelScope.launch {
                searchInteractor
                    .searchTrackList(query)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(foundTrack: List<Track>?, errorMessage: String?) {
        when (errorMessage) {
            null -> {
                val tracks = arrayListOf<Track>()
                if (foundTrack != null) {
                    tracks.addAll(foundTrack)
                }
                _stateLiveData.postValue(SearchState.SearchResult(tracks = tracks))
            }
            Resource.NOT_FOUND -> {
                _stateLiveData.postValue(SearchState.Error(errorMessage))
            }
            else -> {
                _stateLiveData.postValue(SearchState.Error(Resource.CONNECTION_ERROR))
            }
        }

    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(CLICK_ITEM_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    fun searchDebounce(changedText: String) {
        if (lastSearchText != changedText) {
            tracksSearchDebounce(changedText)
            lastSearchText = changedText
        }
    }

    fun saveTrackToHistory(track: Track) {
        searchInteractor.addTrackToHistory(track)
    }

    fun getHistoryList() {
        if (historyIsNotEmpty())
            _stateLiveData.postValue(
                SearchState.HistoryList(
                    searchInteractor.getHistoryList()
                )
            )
    }

    fun clearHistory() {
        searchInteractor.clearHistoryList()
        _stateLiveData.postValue(SearchState.SearchResult(arrayListOf()))
    }

    private fun historyIsNotEmpty(): Boolean {
        return searchInteractor.getHistoryList().isNotEmpty()
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

}