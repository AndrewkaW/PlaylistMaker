package com.practicum.playlistmaker.ui.player.adapter

import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.PLAYLISTS_IMAGE_DIRECTORY
import com.practicum.playlistmaker.domain.playlists.model.Playlist
import java.io.File

class PlaylistLinerViewHolder(parentView: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater
        .from(parentView.context)
        .inflate(R.layout.playlist_planer_item_view, parentView, false)
) {
    private val ivArtwork: ImageView = itemView.findViewById(R.id.iv_planer_art)
    private val tvName: TextView = itemView.findViewById(R.id.tv_playlist_name)
    private val tvCountTrack: TextView = itemView.findViewById(R.id.tv_count_tracks)


    private val filePath = File(
        parentView.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        PLAYLISTS_IMAGE_DIRECTORY
    )

    fun bind(model: Playlist) {
        tvName.text = model.name
        tvCountTrack.text = tvCountTrack.resources.getQuantityString(
            R.plurals.tracks_hint,
            model.numbersOfTrack,
            model.numbersOfTrack,
        )

        if (model.pictureName.isNullOrEmpty()) {
            ivArtwork.setImageResource(R.drawable.default_art_work)
        } else {
            val file = File(filePath, model.pictureName)
            ivArtwork.setImageURI(file.toUri())
        }
    }
}