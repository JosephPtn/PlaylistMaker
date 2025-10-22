package com.saturnnetwork.playlistmaker.ui.player

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.saturnnetwork.playlistmaker.R
import com.saturnnetwork.playlistmaker.di.PlayerInteractorCreator
import com.saturnnetwork.playlistmaker.domain.models.Track
import com.saturnnetwork.playlistmaker.domain.player.PlayerInteractor
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    private var playerState = STATE_DEFAULT

    private lateinit var playbackControlButton: ImageButton
    private lateinit var playbackProgress: TextView

    private val playerInteractor: PlayerInteractor by lazy {
        PlayerInteractorCreator.create()
    }

    private val handler = Handler(Looper.getMainLooper())

    private val updatePlaybackProgressRunnable = object : Runnable {
        override fun run() {
            updatePlaybackProgress()
            handler.postDelayed(this, 300)
        }
    }

    private fun preparePlayer(url: String) {
        playerInteractor.preparePlayer(url,
            onPrepared = {
                playerState = STATE_PREPARED
            },
            onCompletion = {
                playerState = STATE_PREPARED
                playbackProgress.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(0L)
                playbackControlButton.setImageResource(R.drawable.play_button)
                handler.removeCallbacks(updatePlaybackProgressRunnable)
            })
    }

    private fun startPlayer() {
        playerInteractor.play()
        playbackControlButton.setImageResource(R.drawable.pause_button)
        handler.post(updatePlaybackProgressRunnable)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        playerInteractor.pause()
        playbackControlButton.setImageResource(R.drawable.play_button)
        handler.removeCallbacks(updatePlaybackProgressRunnable)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> pausePlayer()
            STATE_PREPARED, STATE_PAUSED -> startPlayer()
        }
    }

    private fun updatePlaybackProgress() {
        if (playerState == STATE_PLAYING) {
            val pos = playerInteractor.getCurrentPosition()
            playbackProgress.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(pos)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)
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

        val trackNamePlayerActivity = findViewById<TextView>(R.id.trackNamePlayerActivity)
        val artistNamePlayerActivity = findViewById<TextView>(R.id.artistNamePlayerActivity)
        playbackProgress = findViewById<TextView>(R.id.playbackProgress)
        val trackDuration_value = findViewById<TextView>(R.id.trackDuration_value)
        val trackAlbum_text = findViewById<TextView>(R.id.trackAlbum_text)
        val trackAlbum_value = findViewById<TextView>(R.id.trackAlbum_value)
        val releaseYear_text = findViewById<TextView>(R.id.releaseYear_text)
        val releaseYear_value = findViewById<TextView>(R.id.releaseYear_value)
        val trackGenre_value = findViewById<TextView>(R.id.trackGenre_value)
        val country_value = findViewById<TextView>(R.id.country_value)

        val btnBackFromPlayer = findViewById<Button>(R.id.btnBackFromPlayer)
        val playbackControlButton = findViewById<ImageButton>(R.id.playbackControlButton)
        this.playbackControlButton = playbackControlButton

        playbackControlButton.setOnClickListener {
            playbackControl()
        }

        btnBackFromPlayer.setOnClickListener {
            finish()
        }

        fun pxToDp(px: Float, context: Context): Float {
            return px / context.resources.displayMetrics.density
        }

        val albumArtView = findViewById<ImageView>(R.id.albumArtView)

        fun getCoverArtwork(url: String) {
            Glide.with(this)
                .load(url)
                .transform(RoundedCorners(pxToDp(8F, this).toInt()))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(albumArtView)
        }

        track?.let {
            if (it.artworkUrl100.isNotEmpty()) {
                getCoverArtwork(it.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            } else {
                albumArtView.setImageResource(R.drawable.placeholder)
            }

            if (it.trackName.isNotBlank()) {
                trackNamePlayerActivity.text = it.trackName
            }
            if (it.artistName.isNotBlank()) {
                artistNamePlayerActivity.text = it.artistName
            }
            if (it.collectionName.isNotBlank()) {
                trackAlbum_value.text = it.collectionName
            } else {
                trackAlbum_value.visibility = View.GONE
                trackAlbum_text.visibility = View.GONE
            }

            it.trackTime?.let { duration ->
                playbackProgress.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(0L)
                trackDuration_value.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(duration)
            }

            if (!it.releaseDate.isNullOrBlank()) {
                val dateTime = ZonedDateTime.parse(it.releaseDate)
                val year = dateTime.year
                releaseYear_value.text = year.toString()
            } else {
                releaseYear_value.visibility = View.GONE
                releaseYear_text.visibility = View.GONE
            }

            if (it.primaryGenreName.isNotBlank()) {
                trackGenre_value.text = it.primaryGenreName
            }

            if (it.country.isNotBlank()) {
                country_value.text = it.country
            }

            if (it.previewUrl.isNotBlank()) {
                preparePlayer(it.previewUrl)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerInteractor.release()
        handler.removeCallbacksAndMessages(null)
    }
}
