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

    fun bind(playlist: Playlist) {
        binding.namePlaylist.text = playlist.name
        binding.trackCount.text = playlist.trackCount.toString()

        if (playlist.coverImagePath.isEmpty()) {
            Glide.with(itemView.context)
                .load(R.drawable.placeholder)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(8)))
                .into(binding.playlistCover)
        } else {
            Glide.with(itemView.context)
                .load(playlist.coverImagePath.toUri())
                .apply(RequestOptions.bitmapTransform(RoundedCorners(8)))
                .into(binding.playlistCover)
        }

        itemView.setOnClickListener {
            onClickItem(playlist.id)
        }
    }


}