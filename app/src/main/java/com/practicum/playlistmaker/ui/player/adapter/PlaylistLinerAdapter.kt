package com.practicum.playlistmaker.ui.player.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.domain.playlists.model.Playlist

class PlaylistLinerAdapter(
    private val clickListener: ClickListener,
) : RecyclerView.Adapter<PlaylistLinerViewHolder>() {

    var playlists = listOf<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistLinerViewHolder {
        return PlaylistLinerViewHolder(parent)
    }

    override fun onBindViewHolder(holder: PlaylistLinerViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener { clickListener.click(playlists[position]) }
    }

    override fun getItemCount(): Int = playlists.size

    fun interface ClickListener {
        fun click(playlist: Playlist)
    }
}