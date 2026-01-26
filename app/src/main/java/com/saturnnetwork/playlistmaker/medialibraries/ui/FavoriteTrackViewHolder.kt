package com.saturnnetwork.playlistmaker.medialibraries.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.saturnnetwork.playlistmaker.R
import com.saturnnetwork.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class FavoriteTrackViewHolder(itemView: View,
                              private val onTrackClick: (Track) -> Unit): RecyclerView.ViewHolder(itemView) {

    private val trackNameImage: ImageView = itemView.findViewById(R.id.trackNameImage)
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)

    private val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main
    )
    private var clickJob: Job? = null

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    fun unbind() {
        scope.coroutineContext.cancelChildren()
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
            if (clickJob?.isActive == true) return@setOnClickListener

            clickJob = scope.launch {
                onTrackClick(track)
                delay(CLICK_DEBOUNCE_DELAY)
            }
        }

    }

}