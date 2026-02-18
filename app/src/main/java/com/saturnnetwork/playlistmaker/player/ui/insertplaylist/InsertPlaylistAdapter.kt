package com.saturnnetwork.playlistmaker.player.ui.insertplaylist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saturnnetwork.playlistmaker.databinding.PlaylistCardInsertTrackBinding
import com.saturnnetwork.playlistmaker.medialibraries.domain.model.Playlist
import com.saturnnetwork.playlistmaker.player.ui.insertplaylist.viewholder.InsertPlaylistViewHolder


class InsertPlaylistAdapter (private var playlists: List<Playlist>,
                             private val onClickItem: (Long) -> Unit
) : RecyclerView.Adapter<InsertPlaylistViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InsertPlaylistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PlaylistCardInsertTrackBinding.inflate(inflater, parent, false)
        return InsertPlaylistViewHolder(binding, onClickItem)
    }

    override fun getItemCount(): Int {
        return playlists.count()
    }

    override fun onBindViewHolder(holder: InsertPlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    fun updateGroups(newPlaylist: List<Playlist>) {
        playlists = newPlaylist
        notifyDataSetChanged()
    }

}