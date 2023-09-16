package com.practicum.playlistmaker.ui.media.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.domain.playlists.model.Playlist

class PlaylistsAdapter : RecyclerView.Adapter<PlaylistsViewHolder>() {
    var playlists = listOf<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        return PlaylistsViewHolder(parent)
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}