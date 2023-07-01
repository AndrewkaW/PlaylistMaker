package com.practicum.playlistmaker.ui.player.activity

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.Track
import com.practicum.playlistmaker.ui.player.view_model.PlayerViewModel
import com.practicum.playlistmaker.ui.search.SearchActivity.Companion.TRACK
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.utils.DateUtils.millisToStrFormat
import com.practicum.playlistmaker.utils.DateUtils.previewUrlSizeChange
import com.practicum.playlistmaker.utils.DateUtils.strDateFormat

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var vmPlayer: PlayerViewModel
    private lateinit var playerInteractor: PlayerInteractor
    private lateinit var track: Track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        track = Gson().fromJson(
            intent.getStringExtra(TRACK),
            Track::class.java
        )

        playerInteractor = Creator.providePlayerInteractor(track, MediaPlayer())
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vmPlayer = ViewModelProvider(
            this,
            PlayerViewModel.getPlayerViewModelFactory(playerInteractor, track)
        )[PlayerViewModel::class.java]

        vmPlayer.playButtonEnabled.observe(this) {
            binding.playButt.isEnabled = it
        }
        binding.playButt.apply {
            setOnClickListener {
                vmPlayer.playbackControl()
            }
        }

        vmPlayer.playButtonImage.observe(this) {
            binding.playButt.setImageResource(it)
        }

        vmPlayer.playTextTime.observe(this) {
            binding.playTime.text = it
        }

        vmPlayer.updateTimeAndButton()

        binding.trackNameText.text = track.trackName

        binding.artistNameText.text = track.artistName

        binding.trackTime.text = millisToStrFormat(track.trackTimeMillis)

        Glide.with(binding.artwork)
            .load(previewUrlSizeChange(track.artworkUrl100))
            .placeholder(R.drawable.default_art_work)
            .transform(RoundedCorners(binding.artwork.resources.getDimensionPixelSize(R.dimen.art_work_radius_player)))
            .into(binding.artwork)

        binding.collectionName.apply {
            this.text = track.collectionName
            this.isVisible = vmPlayer.isCollectionVisible()
        }

        binding.releaseDate.text = strDateFormat(track.releaseDate)

        binding.primaryGenre.text = track.primaryGenreName

        binding.country.text = track.country

        binding.toolbarId.apply {
            setNavigationOnClickListener {
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        vmPlayer.detachView()

    }

    override fun onPause() {
        super.onPause()
        vmPlayer.pausePlayer()
    }
}
