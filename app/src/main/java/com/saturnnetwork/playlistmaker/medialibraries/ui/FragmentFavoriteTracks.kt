package com.saturnnetwork.playlistmaker.medialibraries.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.saturnnetwork.playlistmaker.R
import com.saturnnetwork.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.saturnnetwork.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class FragmentFavoriteTracks: Fragment() {

    companion object {

        fun newInstance() = FragmentFavoriteTracks().apply {

        }
    }

    private val viewModel: MediaLibrariesViewModel by viewModel()

    val onTrackClick: (Track) -> Unit = { track ->
        track.isFavorite = true
        findNavController().navigate(
            R.id.action_mediaLibraryFragment_to_playerFragment,
            Bundle().apply { putSerializable("track", track) }
        )
    }

    /*
   Используем binding для доступа к View, а _binding только для:
   Инициализации в onCreateView()
   Обнуления в onDestroyView()
    */
    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FavorityTracksTrackAdapter

    private fun initRecycler() {
        binding.tracksRecyclerView.layoutManager = GridLayoutManager(
            requireContext(), 1,
            GridLayoutManager.VERTICAL, false
        )

        adapter = FavorityTracksTrackAdapter(arrayListOf(), onTrackClick)
        binding.tracksRecyclerView.adapter = adapter
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
        binding.tracksRecyclerView.visibility = View.INVISIBLE
        binding.emptyPlaceholder.visibility = View.VISIBLE
        binding.titleEmptyPlaceholder.visibility = View.VISIBLE

        viewModel.init()

        viewModel.tracksLiveData.observe(viewLifecycleOwner) { tracks ->
            val reversedList = tracks.reversed()
            if (!tracks.isEmpty()) {
                binding.emptyPlaceholder.visibility = View.INVISIBLE
                binding.titleEmptyPlaceholder.visibility = View.INVISIBLE
                binding.tracksRecyclerView.visibility = View.VISIBLE
                adapter = FavorityTracksTrackAdapter(ArrayList(reversedList), onTrackClick)
                binding.tracksRecyclerView.adapter = adapter
            }
        }

    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}
