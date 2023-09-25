package com.practicum.playlistmaker.domain.playlists

import android.net.Uri
import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.domain.playlists.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    suspend fun addPlaylist(name: String, description: String, pictureUri: Uri?)

    fun getAllPlaylist(): Flow<List<Playlist>>

    fun playlistIsAlready(name: String):Flow<Boolean>

    suspend fun addIdTrackToPlaylist(track: Track, playlist: Playlist)
}