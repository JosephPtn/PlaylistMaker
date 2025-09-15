package com.saturnnetwork.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.net.toUri
import com.google.android.material.switchmaterial.SwitchMaterial
import androidx.core.content.edit

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backBtn = findViewById<Button>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }

        val sharedPrefs = getSharedPreferences("settings_activity_preferences", MODE_PRIVATE)
        val themeSwitcherPosition: Boolean = sharedPrefs.getBoolean("themeSwitcherPosition", false)

        val themeSwitcher: SwitchMaterial =  findViewById<SwitchMaterial>(R.id.themeSwitcher)
        themeSwitcher.isChecked = themeSwitcherPosition

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            sharedPrefs.edit {
                putBoolean("themeSwitcherPosition", checked)
            }
        }

        val shareAppTextView: TextView = findViewById<Button>(R.id.share_app)
        shareAppTextView.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.android_course_url))
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        val contactsSupportTextView: TextView = findViewById<Button>(R.id.contact_support)
        contactsSupportTextView.setOnClickListener {

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email_address)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_email_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.support_email_body))
            }
            startActivity(intent)
        }

        val userAgreementTextView: TextView = findViewById<Button>(R.id.user_agreement)
        userAgreementTextView.setOnClickListener {

            val webpage: Uri = "https://yandex.ru/legal/practicum_offer/".toUri()
            val intent = Intent(Intent.ACTION_VIEW, webpage)
            startActivity(intent)
        }

    }
}