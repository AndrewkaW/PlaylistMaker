package com.practicum.playlistmaker.ui.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModelFactory

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModelFactory(this)
        )[SettingsViewModel::class.java]

        // Бинд кнопки назад на тулбаре
        binding.toolbarId.apply {
            setNavigationOnClickListener {
                finish()
            }
        }

        //Бинд поделитьсчя приложением
        binding.shareAppButt.apply {
            setOnClickListener {
                viewModel.shareApp()
            }
        }

        //Бинд написапть в поддержку
        binding.supportButt.apply {
            setOnClickListener {
                viewModel.openSupport()
            }
        }

        //Бинд откртия пользовательского соглашения
        binding.userAgreementButt.apply {
            setOnClickListener {
                viewModel.openTerms()
            }
        }

        //Бинд свитчера темы
        binding.darkThemeSwitch.apply {
            isChecked = viewModel.isCheckedTheme()
            setOnCheckedChangeListener { _, isChecked -> viewModel.switchTheme(isChecked) }
        }
    }
}