package com.saturnnetwork.playlistmaker.player.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.saturnnetwork.playlistmaker.R
import com.saturnnetwork.playlistmaker.databinding.PlayerFragmentBinding
import com.saturnnetwork.playlistmaker.player.domain.PlayerState
import com.saturnnetwork.playlistmaker.search.domain.models.Track
import com.saturnnetwork.playlistmaker.utils.gone
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.util.Locale

class PlayerFragment: Fragment() {

    private var _binding: PlayerFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlayerViewModel by viewModel()

    fun pxToDp(px: Float, context: Context): Float {
        return px / context.resources.displayMetrics.density
    }

    fun getCoverArtwork(url: String) {
        Glide.with(this)
            .load(url)
            .transform(RoundedCorners(pxToDp(8F, requireContext()).toInt()))
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(binding.albumArtView)
    }

    private fun setTrackData(track: Track?) {
        if (track != null) {
            if (track.artworkUrl100.isNotEmpty()) {
                getCoverArtwork(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            } else {
                binding.albumArtView.setImageResource(R.drawable.placeholder)
            }

            if (track.trackName.isNotBlank()) {
                binding.trackNamePlayerActivity.text = track.trackName
            }
            if (track.artistName.isNotBlank()) {
                binding.artistNamePlayerActivity.text = track.artistName
            }
            if (track.collectionName.isNotBlank()) {
                binding.trackAlbumValue.text = track.collectionName
            } else {
                binding.trackAlbumValue.gone()
                binding.trackAlbumText.gone()
            }

            track.trackTime.let { duration ->
                binding.playbackProgress.text =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(0L)
                binding.trackDurationValue.text =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(duration)
            }

            if (!track.releaseDate.isNullOrBlank()) {
                val dateTime = ZonedDateTime.parse(track.releaseDate)
                val year = dateTime.year
                binding.releaseYearValue.text = year.toString()
            } else {
                listOf(binding.releaseYearValue, binding.releaseYearText).gone()
            }

            if (track.primaryGenreName.isNotBlank()) {
                binding.trackGenreValue.text = track.primaryGenreName
            }

            if (track.country.isNotBlank()) {
                binding.countryValue.text = track.country
            }

        }
    }

    private fun renderState(state: PlayerScreenState) {
        when (state.playerState) {
            PlayerState.PREPARED -> {
                setTrackData(state.track)
                binding.playbackControlButton.setImageResource(R.drawable.play_button)
            }
            PlayerState.PLAYING -> {
                setTrackData(state.track)
                binding.playbackProgress.text = state.playbackPosition
                binding.playbackControlButton.setImageResource(R.drawable.pause_button)
            }
            PlayerState.PAUSED -> {
                setTrackData(state.track)
                binding.playbackProgress.text = state.playbackPosition
                binding.playbackControlButton.setImageResource(R.drawable.play_button)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlayerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val track = arguments?.getSerializable("track") as? Track

        // Устанавливаем трек только если ViewModel ещё пустая
        if (track != null && viewModel.track.value == null) {
            viewModel.setTrack(track)
        }
        viewModel.observeScreenStateLiveData().observe(this) { state ->
            renderState(state)
        }

        binding.playbackControlButton.setOnClickListener {
            viewModel.playbackControl()
        }

        binding.btnBackFromPlayer.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        viewModel.pauseOnBackground()

    }

}