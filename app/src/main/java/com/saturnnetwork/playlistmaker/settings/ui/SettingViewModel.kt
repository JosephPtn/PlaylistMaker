package com.saturnnetwork.playlistmaker.settings.ui

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.saturnnetwork.playlistmaker.settings.domain.SettingsInteractor
import com.saturnnetwork.playlistmaker.utils.livedata.SingleLiveEvent

class SettingViewModel(private val settingsInteractor: SettingsInteractor): ViewModel() {

    private val intentLiveData = SingleLiveEvent<Intent>()
    private val selectedMode = SingleLiveEvent<Boolean>()
    fun observeIntentLiveData(): LiveData<Intent> = intentLiveData
    fun observeSelectedMode(): LiveData<Boolean> = selectedMode
    fun onShareButtonClicked(value: String) {
        intentLiveData.postValue(settingsInteractor.buttonToShareApp(value))
    }
    fun onContactSupportButtonClicked(email: String,
                                      subject: String,
                                      text: String) {
        intentLiveData.postValue(settingsInteractor.buttonToContactSupport(email,subject,text))
    }
    fun onButtonToUserAgreement(url: String) {
        intentLiveData.postValue(settingsInteractor.buttonToUserAgreement(url))
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
