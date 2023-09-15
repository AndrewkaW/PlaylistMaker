package com.practicum.playlistmaker.ui.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.ui.player.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private val playlistVM: PlayerViewModel by viewModel()
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newPlaylistBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mediaLibraryFragment_to_newPlaylistFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        fun newInstance() = PlaylistFragment()

    }
}