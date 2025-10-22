package com.saturnnetwork.playlistmaker

import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){

    private val trackNameImage: ImageView = itemView.findViewById(R.id.trackNameImage)
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    fun bind(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(trackNameImage)

        itemView.setOnClickListener {
            if (clickDebounce()) {
                val sharedPrefs: SharedPreferences = itemView.context.getSharedPreferences("sharedPrefs", MODE_PRIVATE)
                val searchHistory: SearchHistory = SearchHistory(sharedPrefs)
                //searchHistory.clear()
                searchHistory.write(track)
                //println(searchHistory.read())


                val intent = Intent(itemView.context, PlayerActivity::class.java)
                intent.putExtra("track", track)
                itemView.context.startActivity(intent)
            }

        }
    }

}