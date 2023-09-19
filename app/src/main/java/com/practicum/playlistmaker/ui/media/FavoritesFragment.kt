package com.practicum.playlistmaker.ui.media

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavouritesBinding
import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.ui.media.view_model.FavoritesViewModel
import com.practicum.playlistmaker.ui.search.SearchFragment
import com.practicum.playlistmaker.ui.search.adapter.TracksAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private val favoritesVM: FavoritesViewModel by viewModel()

    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    private var adapter: TracksAdapter? = null

    private lateinit var placeholderMessage: TextView
    private lateinit var placeholderImage: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var favoritesList: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TracksAdapter { clickOnTrack(it) }
        favoritesList = binding.favoritesList
        favoritesList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        favoritesList.adapter = adapter

        placeholderMessage = binding.errorTextPh
        placeholderImage = binding.icPh
        progressBar = binding.progressBar

        favoritesVM.fillData()

        favoritesVM.stateLiveData.observe(viewLifecycleOwner) {
            render(it)
        }


    }

    override fun onStart() {
        super.onStart()
        favoritesVM.fillData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter = null
        favoritesList.adapter = null
    }

    private fun clickOnTrack(track: Track) {
        findNavController().navigate(
            R.id.action_mediaLibraryFragment_to_playerFragment,
            Bundle().apply { putSerializable(SearchFragment.TRACK, track) }
        )
    }

    private fun render(state: FavoritesState) {
        when (state) {
            is FavoritesState.Content -> showContent(state.tracks)
            is FavoritesState.Empty -> showEmpty(state.message)
            is FavoritesState.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        favoritesList.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        placeholderImage.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun showEmpty(message: String) {
        favoritesList.visibility = View.GONE
        placeholderMessage.visibility = View.VISIBLE
        placeholderImage.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

        placeholderMessage.text = message
    }

    private fun showContent(tracks: List<Track>) {
        favoritesList.visibility = View.VISIBLE
        placeholderMessage.visibility = View.GONE
        placeholderImage.visibility = View.GONE
        progressBar.visibility = View.GONE

        adapter?.tracks?.clear()
        adapter?.tracks?.addAll(tracks)
        adapter?.invertList()
    }

    companion object {

        fun newInstance() = FavoritesFragment()

    }
}