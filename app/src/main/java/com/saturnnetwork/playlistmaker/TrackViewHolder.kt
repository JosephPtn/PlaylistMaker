package com.saturnnetwork.playlistmaker

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){

    private val trackNameImage: ImageView = itemView.findViewById(R.id.trackNameImage)
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)

    fun bind(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = track.trackTime
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(trackNameImage)

    }
}