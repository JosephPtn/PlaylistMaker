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
        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/

        val findBtn = findViewById<Button>(R.id.findBtn)
        val mediaLibrBtn = findViewById<Button>(R.id.mediaLibrBtn)
        val settingsBtn = findViewById<Button>(R.id.settingsBtn)

        val findBtnOnClickListener: View.OnClickListener = object : View.OnClickListener { override fun onClick(v: View?) {

            val context = v?.context
            val intentToSearchActivity = Intent(context, SearchActivity::class.java)
            context?.startActivity(intentToSearchActivity)
            }
        }
        findBtn.setOnClickListener(findBtnOnClickListener)

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