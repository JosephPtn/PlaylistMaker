package com.saturnnetwork.playlistmaker.medialibraries.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.saturnnetwork.playlistmaker.databinding.FragmentFavoriteTracksBinding

class FragmentFavoriteTracks: Fragment() {

    companion object {

        fun newInstance() = FragmentFavoriteTracks().apply {

        }
    }

    /*
   Используем binding для доступа к View, а _binding только для:
   Инициализации в onCreateView()
   Обнуления в onDestroyView()
    */
    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
