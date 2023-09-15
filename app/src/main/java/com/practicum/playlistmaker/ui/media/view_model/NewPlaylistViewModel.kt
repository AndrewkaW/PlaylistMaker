package com.practicum.playlistmaker.ui.media.view_model

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.playlists.PlaylistsInteractor
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    fun saveData(name: String, description: String, pictureUri: Uri?) {
        viewModelScope.launch {
            playlistsInteractor.addPlaylist(
                name = name,
                description = description,
                pictureUri = pictureUri
            )
        }
    }
}