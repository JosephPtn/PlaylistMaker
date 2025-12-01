package com.saturnnetwork.playlistmaker.medialibraries.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.saturnnetwork.playlistmaker.databinding.FragmentPlaylistsBinding

class FragmentPlaylists: Fragment() {

    companion object {

        fun newInstance() = FragmentPlaylists().apply {

        }
    }

    private lateinit var binding: FragmentPlaylistsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)



        return binding.root
    }

}