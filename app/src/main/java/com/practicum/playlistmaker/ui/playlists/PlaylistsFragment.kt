package com.practicum.playlistmaker.ui.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.domain.playlists.model.Playlist
import com.practicum.playlistmaker.ui.playlists.adapter.PlaylistsAdapter
import com.practicum.playlistmaker.ui.playlists.state.PlaylistsState
import com.practicum.playlistmaker.ui.playlists.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private val vmPlaylist: PlaylistsViewModel by viewModel()
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private var adapter: PlaylistsAdapter? = null

    private var placeholderMessage: TextView? = null
    private var placeholderImage: ImageView? = null
    private var progressBar: ProgressBar? = null
    private var rvPlaylist: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newPlaylistBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mediaLibraryFragment_to_newPlaylistFragment)
        }

        adapter = PlaylistsAdapter { clickOnPlaylist(it) }
        rvPlaylist = binding.rvPlaylists
        rvPlaylist!!.layoutManager = GridLayoutManager(requireContext(), 2)
        rvPlaylist!!.adapter = adapter

        placeholderMessage = binding.tvErrorPh
        placeholderImage = binding.ivImagePh
        progressBar = binding.pbPlaylists

        vmPlaylist.fillData()

        vmPlaylist.stateLiveData.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onResume() {
        super.onResume()
        vmPlaylist.fillData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter = null
        rvPlaylist = null
        progressBar = null
        placeholderImage = null
        placeholderMessage = null
    }

    private fun render(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.Content -> showContent(state.playlists)
            is PlaylistsState.Empty -> showEmpty()
            is PlaylistsState.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        rvPlaylist?.visibility = View.GONE
        placeholderMessage?.visibility = View.GONE
        placeholderImage?.visibility = View.GONE
        progressBar?.visibility = View.VISIBLE
    }

    private fun showEmpty() {
        rvPlaylist?.visibility = View.GONE
        placeholderMessage?.visibility = View.VISIBLE
        placeholderImage?.visibility = View.VISIBLE
        progressBar?.visibility = View.GONE
    }

    private fun showContent(playlists: List<Playlist>) {
        rvPlaylist?.visibility = View.VISIBLE
        placeholderMessage?.visibility = View.GONE
        placeholderImage?.visibility = View.GONE
        progressBar?.visibility = View.GONE

        adapter?.playlists = playlists
        adapter?.notifyDataSetChanged()
    }

    private fun clickOnPlaylist(playlist: Playlist) {
        findNavController().navigate(
            R.id.action_mediaLibraryFragment_to_playlistDetailsFragment,
            Bundle().apply { putSerializable(PLAYLIST, playlist) }
        )
    }

    companion object {

        fun newInstance() = PlaylistsFragment()

        const val PLAYLIST = "PLAYLIST"

    }
}