package com.saturnnetwork.playlistmaker.medialibraries.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.saturnnetwork.playlistmaker.databinding.FragmentFavoriteTracksBinding

class FragmentFavoriteTracks: Fragment() {

    private lateinit var binding: FragmentFavoriteTracksBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)



        return binding.root
    }

}
