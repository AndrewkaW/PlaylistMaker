package com.practicum.playlistmaker.presentation.search

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.Track

class TracksAdapter(
    private val clickListener: ClickListener,
) : RecyclerView.Adapter<TracksViewHolder>() {

    lateinit var tracks: ArrayList<Track>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        return TracksViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { clickListener.click(tracks[position]) }
    }

    override fun getItemCount(): Int = tracks.size

    fun interface ClickListener {
        fun click(track: Track)
    }

}