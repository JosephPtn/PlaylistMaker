package com.saturnnetwork.playlistmaker.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.saturnnetwork.playlistmaker.R
import com.saturnnetwork.playlistmaker.ui.medialibraries.MediaLibrariesActivity
import com.saturnnetwork.playlistmaker.ui.search.SearchActivity
import com.saturnnetwork.playlistmaker.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val findBtn = findViewById<Button>(R.id.findBtn)
        val mediaLibrBtn = findViewById<Button>(R.id.mediaLibrBtn)
        val settingsBtn = findViewById<Button>(R.id.settingsBtn)

        findBtn.setOnClickListener {
            val intentToSearchActivity = Intent(
                this,
                SearchActivity::class.java
            )
            this.startActivity(intentToSearchActivity)

        }

        mediaLibrBtn.setOnClickListener {
            val intentToMediaLibrariesActivity = Intent(
                this,
                MediaLibrariesActivity::class.java
            )
            this.startActivity(intentToMediaLibrariesActivity)

        }

        settingsBtn.setOnClickListener {
            val intentToSettingsActivity = Intent(
                this,
                SettingsActivity::class.java
            )
            this.startActivity(intentToSettingsActivity)

        }

    }
}