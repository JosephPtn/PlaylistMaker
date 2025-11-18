package com.saturnnetwork.playlistmaker.player.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.saturnnetwork.playlistmaker.R
import com.saturnnetwork.playlistmaker.databinding.ActivityPlayerBinding
import com.saturnnetwork.playlistmaker.player.domain.PlayerState
import com.saturnnetwork.playlistmaker.search.domain.models.Track
import com.saturnnetwork.playlistmaker.utils.gone
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private val viewModel: PlayerViewModel by viewModel()

    fun pxToDp(px: Float, context: Context): Float {
        return px / context.resources.displayMetrics.density
    }

    fun getCoverArtwork(url: String) {
        Glide.with(this)
            .load(url)
            .transform(RoundedCorners(pxToDp(8F, this).toInt()))
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val track: Track? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("track", Track::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("track") as? Track
        }

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
            finish()
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.pauseOnBackground()

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
