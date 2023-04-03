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
    val trackTimeMillis: Int, // Продолжительность трека
    val artworkUrl100: String // Ссылка на изображение обложки
)

class TracksViewHolder(parentView: ViewGroup) : RecyclerView.ViewHolder( LayoutInflater.from(parentView.context).inflate(R.layout.track_item_view, parentView, false)){

    private val trackName : TextView = itemView.findViewById(R.id.trackNameText)
    private val artistName : TextView = itemView.findViewById(R.id.artistNameText)
    private val trackTime : TextView = itemView.findViewById(R.id.trackTimeText)
    private val artwork : ImageView = itemView.findViewById(R.id.artwork)

    fun bind(model : Track){
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTimeMillis.toString()
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.default_art_work)
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.art_work_radius)))
            .into(artwork)
    }
}

class TracksAdapter(
    private val tracks : ArrayList<Track>
) : RecyclerView.Adapter<TracksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        return TracksViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int = tracks.size

}
