package com.saturnnetwork.playlistmaker.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saturnnetwork.playlistmaker.R
import com.saturnnetwork.playlistmaker.domain.TracksInteractor
import com.saturnnetwork.playlistmaker.domain.models.Track

class TrackAdapter(
    private val tracks: ArrayList<Track>,
    private val interactor: TracksInteractor
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tracks_view, parent, false)
        return TrackViewHolder(view, interactor)
    }

    override fun getItemCount(): Int {
        return tracks.count()
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

}