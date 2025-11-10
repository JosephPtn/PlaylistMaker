package com.saturnnetwork.playlistmaker.settings.data

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import com.saturnnetwork.playlistmaker.R
import com.saturnnetwork.playlistmaker.settings.domain.SettingsRepository
import androidx.core.content.edit
import androidx.core.net.toUri

class SettingsRepositoryImpl(
    private val sharedPrefs: SharedPreferences
) : SettingsRepository {

    companion object {
        private const val KEY_THEME = "themeSwitcherPosition"
    }

    override fun getThemeSetting(): Boolean {
        return sharedPrefs.getBoolean(KEY_THEME, false)

    }

    override fun saveThemeSetting(enabled: Boolean) {
        sharedPrefs.edit { putBoolean(KEY_THEME, enabled) }
    }

    override fun buttonToShareApp(value: String): Intent {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, value)
            type = "text/plain"
        }
        return Intent.createChooser(sendIntent, null)
    }

    override fun buttonToContactSupport(
        email: String,
        subject: String,
        text: String
    ): Intent {
        val contactSupportIntent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, email)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, text)
        }
        return Intent.createChooser(contactSupportIntent, null)
    }

    override fun buttonToUserAgreement(url: String): Intent {
        val webpage: Uri = url.toUri()
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        return Intent.createChooser(intent, null)
    }
}