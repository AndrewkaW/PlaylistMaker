package com.practicum.playlistmaker.ui.media.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.domain.playlists.PlaylistsInteractor
import com.practicum.playlistmaker.domain.playlists.model.Playlist
import com.practicum.playlistmaker.ui.media.states.PlaylistsDetailsState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PlaylistDetailsViewModel(
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    private val _stateLiveData = MutableLiveData<PlaylistsDetailsState>()
    val stateLiveData: LiveData<PlaylistsDetailsState> = _stateLiveData

    private var playlist: Playlist? = null

    fun showPlayList(idPlaylist: Int) {
        getPlaylistById(idPlaylist) {
            playlist?.let { showInfo(it) }
            showTracks(idPlaylist)
        }
    }

    private fun renderState(state: PlaylistsDetailsState) {
        _stateLiveData.postValue(state)
    }

    private fun timeTracksMillis(list: List<Track>): Int {
        var millis = 0
        list.forEach { track -> millis += track.trackTimeMillis }
        return millis
    }

    fun deleteTrack(trackId: Int) {
        viewModelScope.launch {
            playlist?.id?.let {
                playlistsInteractor.deleteTrackFromPlaylistByIds(trackId, it)
                showTracks(it)
            }
        }
    }

    private fun showInfo(playlist: Playlist) {
        renderState(
            PlaylistsDetailsState.Info(
                name = playlist.name,
                description = playlist.description,
                nameImage = playlist.pictureName
            )
        )
    }

    private fun showTracks(playlistId: Int) {
        viewModelScope.launch {
            val tracks = playlistsInteractor.getTracksByPlaylistId(playlistId).first()
            renderState(
                PlaylistsDetailsState.Tracks(
                    tracksList = tracks,
                    countTracks = tracks.size,
                    timeTracksMillis = timeTracksMillis(tracks)
                )
            )
        }
    }

    fun deletePlaylist(onResultListener: () -> Unit) {
        viewModelScope.launch {
            playlist?.id?.let {
                Log.e("qwe", "$it")
                playlistsInteractor.deletePlaylistById(it)
                onResultListener()
            }
        }
    }

    private fun getPlaylistById(playListId: Int, onResultListener: () -> Unit) {
        viewModelScope.launch {
            playlist = playlistsInteractor.getPlaylistById(playListId)
            onResultListener()
        }
    }

}