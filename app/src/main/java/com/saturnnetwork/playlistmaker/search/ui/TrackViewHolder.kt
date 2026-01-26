package com.saturnnetwork.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder (itemView: View,
                       private val onHistoryTrack: (Track) -> Unit,
                       private val onTrackClick: (Track) -> Unit): RecyclerView.ViewHolder(itemView){

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
        val radiusPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            2f,
            itemView.context.resources.displayMetrics
        ).toInt()
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .transform(RoundedCorners(radiusPx))
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(trackNameImage)

        itemView.setOnClickListener {
            if (clickJob?.isActive == true) return@setOnClickListener

            clickJob = scope.launch {
                withContext(Dispatchers.IO) {
                    onHistoryTrack(track)
                }
                onTrackClick(track)
                delay(CLICK_DEBOUNCE_DELAY)
            }
        }

    }

}