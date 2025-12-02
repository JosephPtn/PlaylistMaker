package com.saturnnetwork.playlistmaker.main.ui


import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.saturnnetwork.playlistmaker.databinding.ActivityMainBinding
import com.saturnnetwork.playlistmaker.settings.ui.SettingsActivity
import com.saturnnetwork.playlistmaker.medialibraries.ui.MediaLibrariesActivity
import com.saturnnetwork.playlistmaker.search.ui.SearchActivity
import kotlin.getValue


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.findBtn.setOnClickListener {
            viewModel.setIntent("search")
        }
        binding.mediaLibrBtn.setOnClickListener {
            viewModel.setIntent("media_library")
        }
        binding.settingsBtn.setOnClickListener {
            viewModel.setIntent("setting")
        }

        viewModel.observeNavigationLiveData().observe(this) { destination ->
            when (destination) {
                "search" -> startActivity(Intent(this, SearchActivity::class.java))
                "media_library" -> startActivity(Intent(this, MediaLibrariesActivity::class.java))
                else -> startActivity(Intent(this, SettingsActivity::class.java))
            }
        }

    }
}