package com.saturnnetwork.playlistmaker.settings.ui

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.saturnnetwork.playlistmaker.databinding.ActivitySettingsBinding
import kotlin.getValue

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.getThemeSetting()

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.shareApp.setOnClickListener {
            viewModel.onShareButtonClicked()
        }
        binding.contactSupport.setOnClickListener {
            viewModel.onContactSupportButtonClicked()
        }
        binding.userAgreement.setOnClickListener {
            viewModel.onButtonToUserAgreement()
        }
        viewModel.observeIntentLiveData().observe(this) { intent ->
            startActivity(intent)
        }
        viewModel.observeSelectedMode().observe(this) {
            binding.themeSwitcher.isChecked = it
            AppCompatDelegate.setDefaultNightMode(
                if (it) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )

        }
        binding.themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setThemeSetting(isChecked)
        }

    }
}