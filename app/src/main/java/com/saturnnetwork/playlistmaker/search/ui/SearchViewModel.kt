package com.saturnnetwork.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saturnnetwork.playlistmaker.R
import com.saturnnetwork.playlistmaker.search.domain.TracksInteractor
import com.saturnnetwork.playlistmaker.search.domain.models.Track

class SearchViewModel(private val interactor: TracksInteractor): ViewModel() {

    private val searchText = MutableLiveData<String>("")

    fun setSearchText(text: String) {
        searchText.value = text
    }

    fun getSearchText(): String = searchText.value ?: ""

    val searchStateLiveData = MutableLiveData<SearchState>()
    fun observeSearchStateLiveData(): LiveData<SearchState> = searchStateLiveData

    private val tracksLiveData = MutableLiveData<ArrayList<Track>>()
    fun observeTracksLiveData(): LiveData<ArrayList<Track>> = tracksLiveData

    private var tracksFromHistory = ArrayList<Track>()
    //fun observeTracksFromHistoryLiveData(): LiveData<ArrayList<Track>> = tracksFromHistoryLiveData

    fun getInteractor(): TracksInteractor {
        return interactor
    }

    fun loadFromHistory() {
        tracksFromHistory = interactor.loadFromHistory()
        if (tracksFromHistory.isNotEmpty()) {
            searchStateLiveData.postValue(SearchState(
                tracks = tracksFromHistory,
                isLoading = false,
                errorMessageRes = null,
                composition = "history")
            )
        }
    }

    fun searchTracks(searchInput: String) {
        if (searchInput.isNotEmpty()) {
            interactor.searchTracks(expression = searchInput,
                consumer = object : TracksInteractor.TracksConsumer {
                    override fun consume(foundTracks: ArrayList<Track>) {
                        if (foundTracks.isNotEmpty()) {
                            searchStateLiveData.postValue(SearchState(
                                tracks = foundTracks,
                                isLoading = false,
                                errorMessageRes = null,
                                composition = "search_result")
                            )

                        } else {
                            searchStateLiveData.postValue(SearchState(
                                tracks = arrayListOf<Track>(),
                                isLoading = false,
                                errorMessageRes = R.string.nothing_was_found,
                                composition = "error")
                            )
                        }
                    }

                    override fun onError() {
                        searchStateLiveData.postValue(SearchState(
                            tracks = arrayListOf<Track>(),
                            isLoading = false,
                            errorMessageRes = R.string.connection_issues,
                            composition = "error")
                        )
                    }

                })
        } else {
            searchStateLiveData.postValue(SearchState(
                tracks = ArrayList<Track>(),
                isLoading = false,
                errorMessageRes = null,
                composition = "search_result")
            )
        }

    }

    fun clearHistory() {
        interactor.clearHistory()
        tracksFromHistory.clear()
        searchStateLiveData.postValue(SearchState(
            tracks = tracksFromHistory,
            isLoading = false,
            errorMessageRes = null,
            composition = "search_result")
        )
    }


}