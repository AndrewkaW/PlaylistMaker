package com.practicum.playlistmaker.ui.media

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.practicum.playlistmaker.ui.media.view_model.NewPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class NewPlaylistFragment : Fragment() {

    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!
    private val vM: NewPlaylistViewModel by viewModel()

    private var pictureUri: Uri? = null
    private var nameTextChanged: Boolean = false
    private var descriptionTextChanged: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        val windowInsetsController = WindowInsetsControllerCompat(requireActivity().window, requireView())
//        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_TOUCH

        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showOrNotClosingDialog()
            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarId.setNavigationOnClickListener {
            showOrNotClosingDialog()
        } //бинд кнопки назад на тулбаре

        binding.etName.doOnTextChanged { text, _, _, _ ->
            binding.btnCreate.isEnabled = !text.isNullOrEmpty()
            nameTextChanged = !text.isNullOrEmpty()
        } // слущатель изменени названия для активации кнопки создания и меняет флаг изменения имени

        binding.etDescription.doOnTextChanged { text, _, _, _ ->
            descriptionTextChanged = !text.isNullOrEmpty()
        } // слушатель меняет флаг изменнения описания

        //регистрируем событие, которое вызывает photo picker
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                //обрабатываем событие выбора пользователем фотографии
                if (uri != null) {
                    binding.ivArtwork.setImageURI(uri)
                    pictureUri = null
                    //saveImageToPrivateStorage(uri)
                } else {
                    pictureUri = null
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        binding.ivArtwork.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } // запуск выбора картинки по нажатию на ImageView


        binding.btnCreate.setOnClickListener {
            val name = binding.etName.text.toString()
            vM.saveData(
                name = name,
                description = binding.etDescription.text.toString(),
                pictureUri = this.pictureUri
            )
            Toast.makeText(
                requireContext(),
                requireContext().getString(R.string.add_new_playlist_massage_1)
                        + name
                        + requireContext().getString(R.string.add_new_playlist_massage_2),
                Toast.LENGTH_LONG
            ).show()
            findNavController().popBackStack()
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showOrNotClosingDialog() {
        val closingDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(requireContext().getString(R.string.finish_create_playlist))
            .setMessage(requireContext().getString(R.string.unsaved_data_lost))
            .setNeutralButton("Отмена") { _, _ ->
                // ничего не делаем
            }.setPositiveButton("Да") { _, _ ->
                findNavController().navigateUp() // переход назад
            }
        if (dataIsFilled()) {
            closingDialog.show()
        } else {
            findNavController().popBackStack()
        }
    }

    private fun dataIsFilled(): Boolean {
        return pictureUri != null || nameTextChanged || descriptionTextChanged
    }

    companion object {

        fun newInstance() = NewPlaylistFragment()

    }
}