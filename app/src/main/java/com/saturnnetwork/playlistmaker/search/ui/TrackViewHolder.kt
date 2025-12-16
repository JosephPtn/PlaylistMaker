package com.saturnnetwork.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.saturnnetwork.playlistmaker.R
import com.saturnnetwork.playlistmaker.search.domain.models.Track
import com.saturnnetwork.playlistmaker.search.domain.TracksInteractor
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder (itemView: View,
                       private val interactor: TracksInteractor,
                       private val onTrackClick: (Track) -> Unit): RecyclerView.ViewHolder(itemView){

    private val trackNameImage: ImageView = itemView.findViewById(R.id.trackNameImage)
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    fun bind(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(trackNameImage)

        itemView.setOnClickListener {
            if (clickDebounce()) {
                interactor.saveToHistory(track)
                onTrackClick(track)
            }

        }
    }

}