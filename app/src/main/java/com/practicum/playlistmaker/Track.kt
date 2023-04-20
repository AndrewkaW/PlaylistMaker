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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

data class Track(
    val trackId: Int, //уникальный номемр трека
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Int, // Продолжительность трека
    val artworkUrl100: String // Ссылка на изображение обложки
)

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
