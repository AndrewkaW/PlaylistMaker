package com.practicum.playlistmaker.domain.playlists.impl

import android.net.Uri
import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.domain.playlists.PlaylistsInteractor
import com.practicum.playlistmaker.domain.playlists.PlaylistsRepository
import com.practicum.playlistmaker.domain.playlists.model.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsInteractorImpl(private val playlistsRepository: PlaylistsRepository) :
    PlaylistsInteractor {

    override suspend fun addPlaylist(name: String, description: String, pictureUri: Uri?) {
        playlistsRepository.addPlaylist(name, description, pictureUri)
    }

    override fun getAllPlaylist(): Flow<List<Playlist>> {
        return playlistsRepository.getAllPlaylist()
    }

    override fun playlistIsAlready(name: String): Flow<Boolean> {
        return playlistsRepository.getAllPlaylist()
            .map { list -> list.any { it.name == name } }
    }

    override suspend fun addIdTrackToPlaylist(track: Track, playlist: Playlist) {
        playlistsRepository.addIdTrackToPlaylist(track, playlist)
    }
}