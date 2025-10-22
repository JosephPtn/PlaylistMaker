package com.saturnnetwork.playlistmaker.ui.search

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.saturnnetwork.playlistmaker.R
import com.saturnnetwork.playlistmaker.data.SearchHistory
import com.saturnnetwork.playlistmaker.domain.TracksInteractor
import com.saturnnetwork.playlistmaker.domain.models.Track
import com.saturnnetwork.playlistmaker.ui.player.PlayerActivity
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder (itemView: View, private val interactor: TracksInteractor): RecyclerView.ViewHolder(itemView){

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

                val intent = Intent(itemView.context, PlayerActivity::class.java)
                intent.putExtra("track", track)
                itemView.context.startActivity(intent)
            }

        }
    }

}