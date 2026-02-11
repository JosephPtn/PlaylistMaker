package com.saturnnetwork.playlistmaker.player.ui.insertplaylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.saturnnetwork.playlistmaker.R
import com.saturnnetwork.playlistmaker.databinding.InsertInPlaylistBinding
import com.saturnnetwork.playlistmaker.player.ui.PlayerViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class InsertInPlayListFragment(): BottomSheetDialogFragment() {

    private var _binding: InsertInPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlayerViewModel by viewModel()

    private lateinit var adapter: InsertPlaylistAdapter

    var trackId: String = ""

    private fun initRecycler(spanCount: Int) {
        binding.recyclerView.layoutManager = GridLayoutManager(
            requireContext(), spanCount,
            GridLayoutManager.VERTICAL, false
        )
        adapter = InsertPlaylistAdapter(
            arrayListOf(),
            onClickItem = { playlistId ->
                viewModel.onClickItem(trackId, playlistId )
            }
        )
        binding.recyclerView.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = InsertInPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val _trackId = arguments?.getString("trackId")
        if (_trackId != null) {
            trackId = _trackId
        }

        initRecycler(1)

        binding.addPlaylistButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_insertInPlayListFragment_to_fragmentCreatePlaylist)
        }

        viewModel.observeEventFlow().observe(viewLifecycleOwner) { state ->
            if (state.olIsExist) {
                Toast.makeText(requireContext(),
                    state.message,
                    Toast.LENGTH_SHORT).show()
            } else if (state.plIsAdded) {
                Toast.makeText(requireContext(),
                    state.message,
                    Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }


        viewModel.observePlaylist().observe(viewLifecycleOwner) { playlists ->
            val layoutManager = binding.recyclerView.layoutManager as? GridLayoutManager
            layoutManager?.requestLayout()
            adapter.updateGroups(playlists)
        }

        viewModel.getAllPlaylists()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getTheme(): Int {
        return R.style.RoundedBottomSheetDialogTheme
    }


}