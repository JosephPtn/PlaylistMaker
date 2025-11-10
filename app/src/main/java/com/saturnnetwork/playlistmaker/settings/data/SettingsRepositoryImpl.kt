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

    override fun buttonToShareApp(): Int {
        return R.string.android_course_url
    }

    override fun buttonToContactSupport(): List<Int> {
        return listOf(R.string.support_email_address,
            R.string.support_email_subject,
            R.string.support_email_body)
    }



    override fun buttonToUserAgreement(): Int {
        return R.string.user_agreement_url
    }
}