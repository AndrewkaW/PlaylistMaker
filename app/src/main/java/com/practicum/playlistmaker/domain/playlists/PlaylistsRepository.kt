package com.practicum.playlistmaker.domain.playlists

import android.net.Uri
import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.domain.playlists.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {

    suspend fun addPlaylist(name: String, description: String, pictureUri: Uri?)

    fun getAllPlaylist(): Flow<List<Playlist>>

    suspend fun addIdTrackToPlaylist(track: Track, playlist: Playlist)

    fun getTracksByPlaylistId(playlistId: Int): Flow<List<Track>>

    suspend fun deleteTrackFromPlaylistByIds(idTrack: Int, idPlaylist: Int)

    suspend fun deletePlaylistById(idPlaylist: Int)

    suspend fun updatePlaylistById(
        idPlaylist: Int,
        name: String,
        description: String,
        pictureUri: Uri?
    )

    suspend fun getPlaylistById(idPlaylist: Int): Playlist
}