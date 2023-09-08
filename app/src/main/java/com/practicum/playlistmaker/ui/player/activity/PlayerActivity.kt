package com.practicum.playlistmaker.ui.player.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.ui.player.view_model.PlayerViewModel
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.ui.search.SearchFragment.Companion.TRACK
import com.practicum.playlistmaker.utils.DateUtils.millisToStrFormat
import com.practicum.playlistmaker.utils.DateUtils.previewUrlSizeChange
import com.practicum.playlistmaker.utils.DateUtils.strDateFormat
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private val playerViewModel: PlayerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        @Suppress("DEPRECATION") val track = intent.getSerializableExtra(TRACK) as Track
        playerViewModel.prepareTrack(track)
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playerViewModel.playButtonEnabled.observe(this) {
            binding.btnPlay.isEnabled = it
        }
        binding.btnPlay.setOnClickListener {
            playerViewModel.playbackControl()
        }

        playerViewModel.playButtonImage.observe(this) {
            binding.btnPlay.setImageResource(it)
        }

        playerViewModel.playTextTime.observe(this) {
            binding.tvPlayTime.text = it
        }

        binding.btnFavorites.setOnClickListener {
            playerViewModel.favoriteButtonFunction()
        }

        playerViewModel.favoriteButton.observe(this) {
            binding.btnFavorites.setImageResource(
                if (it) {
                    R.drawable.ic_is_favorites
                } else {
                    R.drawable.ic_no_favourites
                }
            )
        }

        binding.tvTrackName.text = track.trackName

        binding.tvArtistName.text = track.artistName

        binding.tvTrackTime.text = millisToStrFormat(track.trackTimeMillis)

        Glide.with(binding.ivArtwork)
            .load(previewUrlSizeChange(track.artworkUrl100))
            .placeholder(R.drawable.default_art_work)
            .transform(RoundedCorners(binding.ivArtwork.resources.getDimensionPixelSize(R.dimen.art_work_radius_player)))
            .into(binding.ivArtwork)

        binding.tvCollectionName.apply {
            this.text = track.collectionName
            this.isVisible = track.collectionName.isNotEmpty()
        }

        binding.tvReleaseDate.text = strDateFormat(track.releaseDate)

        binding.tvPrimaryGenre.text = track.primaryGenreName

        binding.tvCountry.text = track.country

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
