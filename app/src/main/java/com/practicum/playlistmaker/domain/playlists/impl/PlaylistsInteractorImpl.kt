package com.practicum.playlistmaker.domain.playlists.impl

import android.net.Uri
import com.practicum.playlistmaker.domain.playlists.PlaylistsInteractor
import com.practicum.playlistmaker.domain.playlists.PlaylistsRepository

class PlaylistsInteractorImpl(private val playlistsRepository: PlaylistsRepository) :
    PlaylistsInteractor {

    override suspend fun addPlaylist(name: String, description: String, pictureUri: Uri?) {
        playlistsRepository.addPlaylist(name, description, pictureUri)
    }
}