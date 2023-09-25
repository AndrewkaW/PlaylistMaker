package com.practicum.playlistmaker.ui.search.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.domain.player.model.Track

class TracksAdapter(
    private val clickListener: ClickListener,
    private val longClickListener: LongClickListener? = null
) : RecyclerView.Adapter<TracksViewHolder>() {

    var tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        return TracksViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { clickListener.click(tracks[position]) }
        holder.itemView.setOnLongClickListener {longClickListener?.click(tracks[position])
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int = tracks.size

    fun interface ClickListener {
        fun click(track: Track)
    }

    fun interface LongClickListener{
        fun click(track: Track)
    }

    fun invertList() {
        tracks.reverse()
        notifyDataSetChanged()
    }

}