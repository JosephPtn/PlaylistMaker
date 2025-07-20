package com.saturnnetwork.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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