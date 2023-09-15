package com.practicum.playlistmaker.domain.playlists

import android.net.Uri

interface PlaylistsInteractor {
    suspend fun addPlaylist(name: String, description: String, pictureUri: Uri?)
}