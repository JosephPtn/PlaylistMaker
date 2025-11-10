package com.saturnnetwork.playlistmaker.settings.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.saturnnetwork.playlistmaker.R
import com.saturnnetwork.playlistmaker.creator.SettingViewModelFactory
import com.saturnnetwork.playlistmaker.databinding.ActivitySettingsBinding
import kotlin.getValue

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingViewModel by viewModels {
        SettingViewModelFactory(
            sharedPreferences = getSharedPreferences("settings_activity_preferences", MODE_PRIVATE)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.getThemeSetting()

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.shareApp.setOnClickListener {
            viewModel.onShareButtonClicked(getString(R.string.android_course_url))
        }
        binding.contactSupport.setOnClickListener {
            viewModel.onContactSupportButtonClicked(getString(R.string.support_email_address),
                getString(R.string.support_email_subject),
                getString(R.string.support_email_body))
        }
        binding.userAgreement.setOnClickListener {
            viewModel.onButtonToUserAgreement("https://yandex.ru/legal/practicum_offer/")
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