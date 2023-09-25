package com.practicum.playlistmaker.ui.media

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.practicum.playlistmaker.domain.PLAYLIST
import com.practicum.playlistmaker.domain.PLAYLISTS_IMAGE_DIRECTORY
import com.practicum.playlistmaker.domain.playlists.model.Playlist
import com.practicum.playlistmaker.ui.media.view_model.NewPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class NewPlaylistFragment : Fragment() {

    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!
    private val vM: NewPlaylistViewModel by viewModel()

    private var pictureUri: Uri? = null
    private var textChanged: Boolean = false

    private var playlist: Playlist? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        @Suppress("DEPRECATION")
        playlist = arguments?.getSerializable(PLAYLIST) as Playlist?
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlist?.let {
            binding.toolbarId.title = getString(R.string.editor)
            binding.btnCreate.text = getString(R.string.save)
            binding.btnCreate.isEnabled = true
            if (!playlist?.pictureName.isNullOrEmpty()) {
                binding.ivArtwork.setImageURI(getImageUriByName(playlist!!.pictureName!!))
            }
            binding.etName.setText(playlist?.name)
            binding.etDescription.setText(playlist?.description)
        }

        //Переопределил нажатие на кнопку назад
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    showOrNotClosingDialog()
                }
            }
        )

        binding.toolbarId.setNavigationOnClickListener {
            showOrNotClosingDialog()
        } //бинд кнопки назад на тулбаре

        binding.etName.doOnTextChanged { text, _, _, _ ->
            binding.btnCreate.isEnabled = !text.isNullOrEmpty()
            textChanged(text)
        } // слущатель изменени названия для активации кнопки создания и меняет флаг изменения имени

        binding.etDescription.doOnTextChanged { text, _, _, _ ->
            textChanged(text)
        } // слушатель меняет флаг изменнения описания

        //регистрируем событие, которое вызывает photo picker
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                //обрабатываем событие выбора пользователем фотографии
                pictureUri = if (uri != null) {
                    binding.ivArtwork.setImageURI(uri)
                    uri
                } else {
                    null
                }
            }

        binding.ivArtwork.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } // запуск выбора картинки по нажатию на ImageView


        binding.btnCreate.setOnClickListener {
            if (playlist == null) {
                addNewPlaylist()
            } else {
                updatePlaylist()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        playlist = null
    }

    private fun addNewPlaylist() {
        val name = binding.etName.text.toString()
        vM.playlistIsAlready(name)
        if (vM.playlistAlready.value == true) {
            Toast.makeText(
                requireContext(),
                requireContext().getString(R.string.playlist_is_already, name),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            vM.createNewPlaylist(
                name = name,
                description = binding.etDescription.text.toString(),
                pictureUri = this.pictureUri
            )
            Toast.makeText(
                requireContext(),
                requireContext().getString(R.string.add_new_playlist_massage, name),
                Toast.LENGTH_SHORT
            ).show()
        }
        findNavController().popBackStack()
    }

    private fun updatePlaylist() {
        playlist?.id?.let {
            vM.updatePlaylist(
                id = it,
                newName = binding.etName.text.toString(),
                newDescription = binding.etDescription.text.toString(),
                pictureUri = this.pictureUri
            ) {
                findNavController().popBackStack()
            }
        }
    }

    private fun showOrNotClosingDialog() {
        val closingDialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialog)
            .setTitle(requireContext().getString(R.string.finish_create_playlist))
            .setMessage(requireContext().getString(R.string.unsaved_data_lost))
            .setNegativeButton(R.string.cancel) { _, _ ->
                // ничего не делаем
            }.setPositiveButton(R.string.yes) { _, _ ->
                findNavController().popBackStack() // переход назад
            }
        playlist?.let {
            closingDialog.setTitle(R.string.finish_update_playlist)
        }
        if (dataIsFilled()) {
            closingDialog.show()
        } else {
            findNavController().popBackStack()
        }
    }

    private fun dataIsFilled(): Boolean {
        return pictureUri != null || textChanged
    }


    private fun getImageUriByName(nameArt: String): Uri {
        val filePath = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            PLAYLISTS_IMAGE_DIRECTORY
        )
        return File(filePath, nameArt).toUri()
    }

    private fun textChanged(text: CharSequence?) {
        textChanged =
            if (playlist == null) {
                !text.isNullOrEmpty()
            } else {
                playlist!!.description != text.toString()
            }
    }

    companion object {

        fun newInstance() = NewPlaylistFragment()

    }
}