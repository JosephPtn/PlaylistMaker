package com.saturnnetwork.playlistmaker.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.saturnnetwork.playlistmaker.App
import com.saturnnetwork.playlistmaker.R
import com.saturnnetwork.playlistmaker.di.ThemeSettingsInteractorCreator
import com.saturnnetwork.playlistmaker.domain.settings.ThemeSettingsInteractor

class SettingsActivity : AppCompatActivity() {

    private lateinit var interactor: ThemeSettingsInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sharedPrefs = getSharedPreferences("settings_activity_preferences", MODE_PRIVATE)
        interactor = ThemeSettingsInteractorCreator.create(sharedPrefs)

        val backBtn = findViewById<Button>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }

        val themeSwitcher: SwitchMaterial = findViewById(R.id.themeSwitcher)
        themeSwitcher.isChecked = interactor.isDarkModeEnabled()

        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            (applicationContext as App).switchTheme(isChecked)
            interactor.setDarkMode(isChecked)
        }

        val shareAppTextView: TextView = findViewById(R.id.share_app)
        shareAppTextView.setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.android_course_url))
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        val contactsSupportTextView: TextView = findViewById(R.id.contact_support)
        contactsSupportTextView.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email_address)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_email_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.support_email_body))
            }
            startActivity(intent)
        }

        val userAgreementTextView: TextView = findViewById(R.id.user_agreement)
        userAgreementTextView.setOnClickListener {
            val webpage: Uri = Uri.parse("https://yandex.ru/legal/practicum_offer/")
            val intent = Intent(Intent.ACTION_VIEW, webpage)
            startActivity(intent)
        }
    }
}
