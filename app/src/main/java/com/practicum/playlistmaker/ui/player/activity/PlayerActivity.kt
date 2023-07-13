package com.practicum.playlistmaker.ui.player.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.ui.player.view_model.PlayerViewModel
import com.practicum.playlistmaker.ui.search.activity.SearchActivity.Companion.TRACK
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.utils.DateUtils.millisToStrFormat
import com.practicum.playlistmaker.utils.DateUtils.previewUrlSizeChange
import com.practicum.playlistmaker.utils.DateUtils.strDateFormat


class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var playerViewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        @Suppress("DEPRECATION")
        val track = intent.getSerializableExtra(TRACK) as Track
        val playerInteractor = Creator.providePlayerInteractor(track)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playerViewModel = ViewModelProvider(
            this,
            PlayerViewModel.getPlayerViewModelFactory(playerInteractor, track)
        )[PlayerViewModel::class.java]

        playerViewModel.playButtonEnabled.observe(this) {
            binding.playBtn.isEnabled = it
        }
        binding.playBtn.setOnClickListener {
                playerViewModel.playbackControl()
            }
        

        playerViewModel.playButtonImage.observe(this) {
            binding.playBtn.setImageResource(it)
        }

        playerViewModel.playTextTime.observe(this) {
            binding.playTime.text = it
        }

        playerViewModel.conditionPlayButton()

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
            this.isVisible = playerViewModel.isCollectionVisible()
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

    override fun onPause() {
        super.onPause()
        playerViewModel.pausePlayer()
    }
}
