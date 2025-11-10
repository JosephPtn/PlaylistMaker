package com.saturnnetwork.playlistmaker.main.ui


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.saturnnetwork.playlistmaker.utils.livedata.SingleLiveEvent


class MainViewModel: ViewModel() {

    private val navigationLiveData = SingleLiveEvent<String>()
    fun observeNavigationLiveData(): LiveData<String> = navigationLiveData


    fun setIntent(value: String) {
        when (value) {
            "search" -> navigationLiveData.postValue("search")
            "media_library" -> navigationLiveData.postValue("media_library")
            else -> navigationLiveData.postValue("setting")
        }
    }

}