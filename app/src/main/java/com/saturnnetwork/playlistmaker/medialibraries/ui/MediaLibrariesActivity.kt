package com.saturnnetwork.playlistmaker.medialibraries.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.saturnnetwork.playlistmaker.R
import com.saturnnetwork.playlistmaker.databinding.ActivityMediaLibrariesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaLibrariesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaLibrariesBinding
    private val viewModel: MediaLibrariesViewModel by viewModel()
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibrariesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = MediaLibrariesViewPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()

        binding.backBtn.setOnClickListener {
            finish()
        }

    }


}