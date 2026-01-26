package com.saturnnetwork.playlistmaker.medialibraries.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saturnnetwork.playlistmaker.R
import com.saturnnetwork.playlistmaker.search.domain.models.Track

class FavorityTracksTrackAdapter(private val tracks: ArrayList<Track>,
                                 private val onTrackClick: (Track) -> Unit): RecyclerView.Adapter<FavoriteTrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteTrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tracks_view, parent, false)
        return FavoriteTrackViewHolder(view, onTrackClick)
    }

    override fun getItemCount(): Int {
        return tracks.count()
    }

    override fun onBindViewHolder(holder: FavoriteTrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun onViewRecycled(holder: FavoriteTrackViewHolder) {
        holder.unbind()
    }

}