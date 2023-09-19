package com.practicum.playlistmaker.ui.player.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.ui.player.view_model.PlayerViewModel
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.domain.playlists.model.Playlist
import com.practicum.playlistmaker.ui.media.NewPlaylistFragment
import com.practicum.playlistmaker.ui.player.adapter.PlaylistLinerAdapter
import com.practicum.playlistmaker.ui.search.SearchFragment.Companion.TRACK
import com.practicum.playlistmaker.utils.DateUtils.millisToStrFormat
import com.practicum.playlistmaker.utils.DateUtils.previewUrlSizeChange
import com.practicum.playlistmaker.utils.DateUtils.strDateFormat
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private val vmPlayer: PlayerViewModel by viewModel()

    private var rvPlaylist: RecyclerView? = null
    private val adapterPlaylist = PlaylistLinerAdapter { clickOnPlaylist(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        @Suppress("DEPRECATION")
        val track = intent.getSerializableExtra(TRACK) as Track
        vmPlayer.prepareTrack(track)
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vmPlayer.playButtonEnabled.observe(this) {
            binding.btnPlay.isEnabled = it
        }
        binding.btnPlay.setOnClickListener {
            vmPlayer.playbackControl()
        }

        vmPlayer.playButtonImage.observe(this) {
            binding.btnPlay.setImageResource(it)
        }

        vmPlayer.playTextTime.observe(this) {
            binding.tvPlayTime.text = it
        }

        binding.btnFavorites.setOnClickListener {
            vmPlayer.favoriteButtonFunction()
        }

        vmPlayer.favoriteButton.observe(this) {
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


        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetPlayer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.visibility = View.VISIBLE
                binding.overlay.alpha = slideOffset
            }
        })

        binding.btnAddToPlaylist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        rvPlaylist = binding.rvPlaylist
        rvPlaylist!!.layoutManager = LinearLayoutManager(this)
        rvPlaylist!!.adapter = adapterPlaylist

        vmPlayer.playlists.observe(this){
            adapterPlaylist.playlists = it
            adapterPlaylist.notifyDataSetChanged()
        }

        vmPlayer.getPlaylists()

        binding.newPlaylistBtn.setOnClickListener {
            val newPlaylistFragment = NewPlaylistFragment()
            val fragmentManager = supportFragmentManager
//                         fragmentManager
//                .beginTransaction()
//                .replace(R.id.fragment_container_player,newPlaylistFragment)
//                .addToBackStack(null)
//                .commit()
        }
    }

    private fun clickOnPlaylist(playlist: Playlist) {
        vmPlayer.addIdTrackToPlaylist(playlist)
    }

    override fun onPause() {
        super.onPause()
        vmPlayer.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        rvPlaylist = null
    }
}
