package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

data class Track(
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTime: String, // Продолжительность трека
    val artworkUrl100: String // Ссылка на изображение обложки
)

class TracksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    private val trackName : TextView = itemView.findViewById(R.id.trackNameText)
    private val artistName : TextView = itemView.findViewById(R.id.artistNameText)
    private val trackTime : TextView = itemView.findViewById(R.id.trackTimeText)
    private val artwork : ImageView = itemView.findViewById(R.id.artwork)

    fun bind(model : Track){
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTime
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.default_art_work)
            .transform(RoundedCorners(2))
            .into(artwork)
    }
}

class TracksAdapter(
    private val tracks : List<Track>
) : RecyclerView.Adapter<TracksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.track_item_view,parent,false)
        return TracksViewHolder(view)

    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int = tracks.size
}
