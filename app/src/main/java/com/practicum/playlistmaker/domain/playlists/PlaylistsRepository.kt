package com.practicum.playlistmaker.domain.playlists

import android.net.Uri
import com.practicum.playlistmaker.domain.playlists.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {

    suspend fun addPlaylist(name: String, description: String, pictureUri: Uri?)

    suspend fun deletePlaylist(playlist: Playlist)

    fun getAllPlaylist(): Flow<List<Playlist>>

}