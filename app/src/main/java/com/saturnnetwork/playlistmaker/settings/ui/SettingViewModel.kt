package com.saturnnetwork.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.saturnnetwork.playlistmaker.settings.domain.SettingsInteractor
import com.saturnnetwork.playlistmaker.utils.livedata.SingleLiveEvent

class SettingViewModel(private val settingsInteractor: SettingsInteractor,
                       private val resourceProvider: ResourceProvider): ViewModel() {

    private val intentLiveData = SingleLiveEvent<Intent>()
    private val selectedMode = SingleLiveEvent<Boolean>()
    fun observeIntentLiveData(): LiveData<Intent> = intentLiveData
    fun observeSelectedMode(): LiveData<Boolean> = selectedMode
    fun onShareButtonClicked() {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, resourceProvider.getString(settingsInteractor.buttonToShareApp()))
            type = "text/plain"
        }
        intentLiveData.postValue(Intent.createChooser(intent, null))
    }
    fun onContactSupportButtonClicked() {
        val mailListParam = settingsInteractor.buttonToContactSupport()
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, resourceProvider.getString(mailListParam[0]))
            putExtra(Intent.EXTRA_SUBJECT, resourceProvider.getString(mailListParam[1]))
            putExtra(Intent.EXTRA_TEXT, resourceProvider.getString(mailListParam[2]))
        }
        intentLiveData.postValue(Intent.createChooser(intent, null))
    }
    fun onButtonToUserAgreement() {
        val webpage: Uri = resourceProvider.getString(settingsInteractor.buttonToUserAgreement()).toUri()
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        intentLiveData.postValue(Intent.createChooser(intent, null))
    }
    fun getThemeSetting(): Boolean {
        val themeMode = settingsInteractor.isDarkModeEnabled()
        selectedMode.postValue(themeMode)
        return themeMode
    }
    fun setThemeSetting(value: Boolean) {
        selectedMode.postValue(value)
        settingsInteractor.setDarkMode(value)
    }


}
