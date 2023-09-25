package com.practicum.playlistmaker.ui.media.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.favorites.FavoritesInteractor
import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.ui.media.states.FavoritesState
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {

    private val _stateLiveData = MutableLiveData<FavoritesState>()
    val stateLiveData: LiveData<FavoritesState> = _stateLiveData

    fun fillData() {
        renderState(FavoritesState.Loading)
        viewModelScope.launch {
            favoritesInteractor
                .getFavoritesList()
                .collect { track ->
                    processResult(track)
                }
        }
    }

    private fun processResult(movies: List<Track>) {
        if (movies.isEmpty()) {
            renderState(FavoritesState.Empty)
        } else {
            renderState(FavoritesState.Content(movies))
        }
    }

    private fun renderState(state: FavoritesState) {
        _stateLiveData.postValue(state)
    }
}