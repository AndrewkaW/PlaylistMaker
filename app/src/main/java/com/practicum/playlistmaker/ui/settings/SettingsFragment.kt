package com.practicum.playlistmaker.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.shareAppButt.apply {
            setOnClickListener {
                viewModel.shareApp()
            }
        }

        binding.supportButt.apply {
            setOnClickListener {
                viewModel.openSupport()
            }
        }

        binding.userAgreementButt.apply {
            setOnClickListener {
                viewModel.openTerms()
            }
        }

        binding.darkThemeSwitch.apply {
            isChecked = viewModel.isCheckedTheme()
            setOnCheckedChangeListener { _, isChecked -> viewModel.switchTheme(isChecked) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}