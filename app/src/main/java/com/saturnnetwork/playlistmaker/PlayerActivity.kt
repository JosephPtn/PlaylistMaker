package com.saturnnetwork.playlistmaker

import android.os.Bundle
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val intent = intent
        val jsonTrack = intent.getStringExtra("jsonTrack")
        val type = object : TypeToken<Track>() {}.type
        val track =  Gson().fromJson<Track>(jsonTrack, type)

        val trackNamePlayerActivity = findViewById<TextView>(R.id.trackNamePlayerActivity)
        val artistNamePlayerActivity = findViewById<TextView>(R.id.artistNamePlayerActivity)
        val playbackProgress = findViewById<TextView>(R.id.playbackProgress)
        val trackDuration_text = findViewById<TextView>(R.id.trackDuration_text)
        val trackDuration_value = findViewById<TextView>(R.id.trackDuration_value)
        val trackAlbum_text = findViewById<TextView>(R.id.trackAlbum_text)
        val trackAlbum_value = findViewById<TextView>(R.id.trackAlbum_value)
        val releaseYear_text = findViewById<TextView>(R.id.releaseYear_text)
        val releaseYear_value = findViewById<TextView>(R.id.releaseYear_value)
        val trackGenre_text = findViewById<TextView>(R.id.trackGenre_text)
        val trackGenre_value = findViewById<TextView>(R.id.trackGenre_value)
        val country_text = findViewById<TextView>(R.id.country_text)
        val country_value = findViewById<TextView>(R.id.country_value)

        val btnBackFromPlayer = findViewById<Button>(R.id.btnBackFromPlayer)
        val addToPlaylistButton = findViewById<ImageButton>(R.id.addToPlaylistButton)
        val playbackControlButton = findViewById<ImageButton>(R.id.playbackControlButton)
        val addToFavoritesButton = findViewById<ImageButton>(R.id.addToFavoritesButton)

        val albumArtView = findViewById<ImageView>(R.id.albumArtView)

        btnBackFromPlayer.setOnClickListener {
            finish()
        }

        fun getCoverArtwork(url: String) {
            Glide.with(this)
                .load(url)
                .transform(RoundedCorners(2))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(albumArtView)
        }

        if (!track.artworkUrl100.isEmpty()) {
            getCoverArtwork(track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
        } else {
            albumArtView.setImageResource(R.drawable.placeholder)
        }

        //Track(trackName=Here Comes the Weekend (feat. Eminem), artistName=P!nk, trackTime=265424, artworkUrl100=https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/2c/b0/de/2cb0de7b-4559-d885-36f8-271c950cba34/886443562097.jpg/100x100bb.jpg, trackId=545398146, collectionName=The Truth About Love, releaseDate=2012-09-14T07:00:00Z, primaryGenreName=Pop, country=USA)
        if (track.trackName.isNotBlank()) {
            trackNamePlayerActivity.text = track.trackName
        }
        if (track.artistName.isNotBlank()) {
            artistNamePlayerActivity.text = track.artistName
        }
        if (track.collectionName.isNotBlank()) {
            trackAlbum_value.text = track.collectionName
        } else {
            trackAlbum_value.visibility = View.GONE
            trackAlbum_text.visibility = View.GONE
        }

        track.trackTime?.let {
            playbackProgress.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(it)
            trackDuration_value.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)
        }

        if (track.releaseDate.isNotBlank()) {
            val dateTime = ZonedDateTime.parse(track.releaseDate)
            val year = dateTime.year
            releaseYear_value.text = year.toString()
        } else {
            releaseYear_value.visibility = View.GONE
            releaseYear_text.visibility = View.GONE
        }

        if (track.primaryGenreName.isNotBlank()) {
            trackGenre_value.text = track.primaryGenreName
        }

        if (track.country.isNotBlank()) {
            country_value.text = track.country
        }


    }
}