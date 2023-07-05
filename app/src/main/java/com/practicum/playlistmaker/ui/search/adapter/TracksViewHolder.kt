package com.practicum.playlistmaker.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.Track
import com.practicum.playlistmaker.utils.DateUtils

class TracksViewHolder(parentView: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context).inflate(R.layout.track_item_view, parentView, false)
) {

    private val trackName: TextView = itemView.findViewById(R.id.trackNameText)
    private val artistName: TextView = itemView.findViewById(R.id.artistNameText)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTimeText)
    private val artwork: ImageView = itemView.findViewById(R.id.artwork)

    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = DateUtils.millisToStrFormat(model.trackTimeMillis)
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.default_art_work)
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.art_work_radius)))
            .into(artwork)
    }
}