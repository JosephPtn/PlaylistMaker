package com.saturnnetwork.playlistmaker.player.ui.insertplaylist.viewholder

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.saturnnetwork.playlistmaker.R
import com.saturnnetwork.playlistmaker.databinding.PlaylistCardInsertTrackBinding
import com.saturnnetwork.playlistmaker.medialibraries.domain.model.Playlist


class InsertPlaylistViewHolder (private val binding: PlaylistCardInsertTrackBinding,
                          private val onClickItem: (Long) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun trackCount(count: Int): String {
        val tracksForm = when {
            count % 10 == 1 && count % 100 != 11 -> "трек"
            count % 10 in 2..4 && (count % 100 !in 12..14) -> "трека"
            else -> "треков"
        }
        return "$count $tracksForm"
    }

    fun bind(playlist: Playlist) {
        binding.namePlaylist.text = playlist.name
        binding.trackCount.text = trackCount(playlist.trackCount.toInt())

        Glide.with(itemView.context)
            .load(playlist.coverImagePath.toUri())
            .placeholder(R.drawable.placeholder)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(8)))
            .into(binding.playlistCover)

        itemView.setOnClickListener {
            onClickItem(playlist.id)
        }
    }


}