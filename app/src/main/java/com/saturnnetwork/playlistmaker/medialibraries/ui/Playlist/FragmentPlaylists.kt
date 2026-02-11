package com.saturnnetwork.playlistmaker.medialibraries.ui.Playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.saturnnetwork.playlistmaker.R
import com.saturnnetwork.playlistmaker.databinding.FragmentPlaylistsBinding
import com.saturnnetwork.playlistmaker.medialibraries.ui.MediaLibrariesViewModel
import com.saturnnetwork.playlistmaker.medialibraries.ui.Playlist.PlaylistAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentPlaylists: Fragment() {

    companion object {

        fun newInstance() = FragmentPlaylists().apply {

        }
    }

    /*
     Используем binding для доступа к View, а _binding только для:
     Инициализации в onCreateView()
     Обнуления в onDestroyView()
    */
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MediaLibrariesViewModel by viewModel()

    private lateinit var adapter: PlaylistAdapter

    private fun initRecycler(spanCount: Int) {
        binding.recyclerView.layoutManager = GridLayoutManager(
            requireContext(), spanCount,
            GridLayoutManager.VERTICAL, false
        )


        adapter = PlaylistAdapter(
            arrayListOf(),
            onClickItem = { item -> }
        )
        binding.recyclerView.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler(2)

        viewModel.getAllPlaylists()

        binding.addPlaylistButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_fragmentCreatePlaylist)
        }

        viewModel.observePlaylist().observe(viewLifecycleOwner) { playlists ->
            if (playlists.isNotEmpty()) {
                //println(state.playlist)
                binding.emptyPlaceholder.visibility = View.INVISIBLE
                binding.titleEmptyPlaceholder.visibility = View.INVISIBLE
                binding.recyclerView.visibility = View.VISIBLE
                val layoutManager = binding.recyclerView.layoutManager as? GridLayoutManager
                layoutManager?.requestLayout()
                adapter.updateGroups(playlists)
            } else {
                binding.emptyPlaceholder.visibility = View.VISIBLE
                binding.titleEmptyPlaceholder.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.INVISIBLE
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}