package com.saturnnetwork.playlistmaker.medialibraries.ui.Playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saturnnetwork.playlistmaker.databinding.PlaylistCardBinding
import com.saturnnetwork.playlistmaker.medialibraries.domain.model.Playlist
import com.saturnnetwork.playlistmaker.medialibraries.ui.Playlist.viewholder.PlaylistViewHolder

class PlaylistAdapter (private var playlists: List<Playlist>,
                       private val onClickItem: (Int) -> Unit
) : RecyclerView.Adapter<PlaylistViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PlaylistCardBinding.inflate(inflater, parent, false)
        return PlaylistViewHolder(binding, onClickItem)
    }

    override fun getItemCount(): Int {
        return playlists.count()
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    fun updateGroups(newPlaylist: List<Playlist>) {
        playlists = newPlaylist
        notifyDataSetChanged()
    }


}