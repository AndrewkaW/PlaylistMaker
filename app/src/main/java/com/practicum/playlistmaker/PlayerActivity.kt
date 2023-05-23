package com.practicum.playlistmaker

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.DateUtils.millisToStrFormat
import com.practicum.playlistmaker.SearchActivity.Companion.TRACK
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity() {

    private lateinit var playBtn: ImageView
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var trackTime: TextView
    private lateinit var artwork: ImageView
    private lateinit var collectionName: TextView
    private lateinit var collectionTitle: TextView
    private lateinit var releaseDate: TextView
    private lateinit var primaryGenreName: TextView
    private lateinit var country: TextView
    private lateinit var track: Track
    private lateinit var playTimeText: TextView

    private var mainThreadHandler: Handler? = null

    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_id).setNavigationOnClickListener {
            finish()
        } // реализация кнопки назад

        mainThreadHandler = Handler(Looper.getMainLooper())

        track = Gson().fromJson(intent.getStringExtra(TRACK), Track::class.java)

        trackName = findViewById(R.id.trackNameText)
        trackName.text = track.trackName

        artistName = findViewById(R.id.artistNameText)
        artistName.text = track.artistName

        trackTime = findViewById(R.id.trackTime)
        trackTime.text = millisToStrFormat(track.trackTimeMillis)

        artwork = findViewById(R.id.artwork)
        Glide.with(artwork)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.default_art_work)
            .transform(RoundedCorners(artwork.resources.getDimensionPixelSize(R.dimen.art_work_radius_player)))
            .into(artwork)

        collectionName = findViewById(R.id.collectionName)

        collectionTitle = findViewById(R.id.collectionNameTitle)
        if (track.collectionName.isEmpty()) {
            collectionName.visibility = View.GONE
            collectionTitle.visibility = View.GONE
        } else {
            collectionName.text = track.collectionName
        }

        releaseDate = findViewById(R.id.releaseDate)
        val formatDate = SimpleDateFormat("yyyy", Locale.getDefault()).parse(track.releaseDate)
        val data = SimpleDateFormat("yyyy", Locale.getDefault()).format(formatDate)
        releaseDate.text = data

        primaryGenreName = findViewById(R.id.primaryGenre)
        primaryGenreName.text = track.primaryGenreName

        country = findViewById(R.id.country)
        country.text = track.country

        //воспроизведение отрывоко трека
        playBtn = findViewById(R.id.play_butt)
        preparePlayer()
        playBtn.setOnClickListener {
            playbackControl()
        }
        playTimeText = findViewById(R.id.play_time)

    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        mainThreadHandler?.removeCallbacksAndMessages(null)
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playBtn.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playBtn.setImageResource(R.drawable.ic_play)
            playTimeText.text = getText(R.string.default_play_time)
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playBtn.setImageResource(R.drawable.ic_pause)
        playerState = STATE_PLAYING
        mainThreadHandler?.postDelayed(
            object : Runnable {
                override fun run() {
                    // Обновляем время
                    playTimeText.text = if (mediaPlayer.currentPosition < REFRESH_PLAY_TIME) {
                        millisToStrFormat(mediaPlayer.currentPosition)
                    } else {
                        getText(R.string.default_play_time)
                    }
                    // И снова планируем то же действие через 0.5 сек
                    mainThreadHandler?.postDelayed(
                        this,
                        DELAY,
                    )
                }
            },
            DELAY
        )
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playBtn.setImageResource(R.drawable.ic_play)
        playerState = STATE_PAUSED
        mainThreadHandler?.removeCallbacksAndMessages(null)
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 500L
        private const val REFRESH_PLAY_TIME = 29900L
    }

}