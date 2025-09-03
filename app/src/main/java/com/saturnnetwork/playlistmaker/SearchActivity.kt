package com.saturnnetwork.playlistmaker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.VelocityTracker
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saturnnetwork.playlistmaker.data_class.SearchResponse
import com.saturnnetwork.playlistmaker.interface_.ItunesApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class SearchActivity : AppCompatActivity() {

    private var globalSearchText: String = ""
    private lateinit var searchInput: EditText

    private lateinit var imgError: ImageView
    private lateinit var textError: TextView
    private lateinit var retryButton: Button

    private lateinit var clear_history_button: Button

    private lateinit var youSearched: TextView

    private lateinit var searchProgressBar: ProgressBar

    private val tracks: ArrayList<Track> = ArrayList()

    private lateinit var sharedPrefs: SharedPreferences

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TrackAdapter
    private lateinit var adapterSearchHistory: TrackAdapter

    private var tracksHistory: ArrayList<Track> = ArrayList()

    private val prefsListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        when (key) {
            "prefsHistory" -> {
                val searchHistory: SearchHistory = SearchHistory(sharedPrefs)
                tracksHistory = searchHistory.read()
            }
        }
    }

    /*private val tracks: ArrayList<Track> = arrayListOf(
        Track(
            trackName = "Smells Like Teen Spirit",
            artistName = "Nirvana",
            trackTime = "5:01",
            artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
        ),
        Track(
            trackName = "Billie Jean",
            artistName = "Michael Jackson",
            trackTime = "4:35",
            artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
        ),
        Track(
            trackName = "Stayin' Alive",
            artistName = "Bee Gees",
            trackTime = "4:10",
            artworkUrl100 = "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
        ),
        Track(
            trackName = "Whole Lotta Love",
            artistName = "Led Zeppelin",
            trackTime = "5:33",
            artworkUrl100 = "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
        ),
        Track(
            trackName = "Sweet Child O'Mine",
            artistName = "Guns N' Roses",
            trackTime = "5:03",
            artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
        )
    )*/

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesApiService::class.java)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        sharedPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        sharedPrefs.registerOnSharedPreferenceChangeListener(prefsListener)
        val searchHistory: SearchHistory = SearchHistory(sharedPrefs)
        tracksHistory = searchHistory.read()

        imgError = findViewById(R.id.imgError)
        imgError.visibility = View.INVISIBLE
        textError = findViewById(R.id.textError)
        textError.visibility = View.INVISIBLE
        retryButton = findViewById(R.id.retry_button)
        retryButton.visibility = View.INVISIBLE

        clear_history_button = findViewById(R.id.clear_history_button)
        clear_history_button.visibility = View.INVISIBLE

        searchProgressBar = findViewById(R.id.searchProgressBar)
        searchProgressBar.visibility = View.INVISIBLE


        youSearched = findViewById(R.id.youSearched)

        recyclerView = findViewById<RecyclerView>(R.id.tracksRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val btnBackFromSearch = findViewById<Button>(R.id.btnBackFromSearch)
        btnBackFromSearch.setOnClickListener {
            finish()
        }

        searchInput = findViewById<EditText>(R.id.searchInput)

        fun updateUIComposition(composition: String) {
            when (composition) {
                "search_history" -> {
                    if (tracksHistory.isNotEmpty()) {
                        youSearched.visibility = View.VISIBLE
                        recyclerView.visibility = View.VISIBLE
                        clear_history_button.visibility = View.VISIBLE

                        val constraintSet = ConstraintSet()
                        val constraintLayout = findViewById<ConstraintLayout>(R.id.activity_search)

                        constraintSet.clone(constraintLayout)

                        constraintSet.connect(
                            R.id.tracksRecyclerView,
                            ConstraintSet.TOP,
                            R.id.youSearched,
                            ConstraintSet.BOTTOM,
                            (8 * resources.displayMetrics.density).toInt()
                        )

                        constraintSet.connect(
                            R.id.tracksRecyclerView,
                            ConstraintSet.BOTTOM,
                            R.id.clear_history_button,
                            ConstraintSet.BOTTOM,
                            (24 * resources.displayMetrics.density).toInt()
                        )

                        constraintSet.connect(
                            R.id.tracksRecyclerView,
                            ConstraintSet.START,
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.START
                        )

                        constraintSet.connect(
                            R.id.tracksRecyclerView,
                            ConstraintSet.END,
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.END
                        )

                        constraintSet.applyTo(constraintLayout)
                        adapterSearchHistory = TrackAdapter(tracksHistory)
                        recyclerView.adapter = adapterSearchHistory
                    }

                }

                "search_result" -> {
                    youSearched.visibility = View.INVISIBLE
                    recyclerView.visibility = View.VISIBLE
                    clear_history_button.visibility = View.INVISIBLE

                    val constraintSet = ConstraintSet()
                    val constraintLayout = findViewById<ConstraintLayout>(R.id.activity_search)

                    constraintSet.clone(constraintLayout)

                    constraintSet.connect(
                        R.id.tracksRecyclerView,
                        ConstraintSet.TOP,
                        R.id.searchFieldContainer,
                        ConstraintSet.BOTTOM,
                        (16 * resources.displayMetrics.density).toInt()
                    )

                    constraintSet.connect(
                        R.id.tracksRecyclerView,
                        ConstraintSet.BOTTOM,
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.BOTTOM
                    )

                    constraintSet.connect(
                        R.id.tracksRecyclerView,
                        ConstraintSet.START,
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.START
                    )

                    constraintSet.connect(
                        R.id.tracksRecyclerView,
                        ConstraintSet.END,
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.END
                    )

                    recyclerView.adapter = adapter
                    constraintSet.applyTo(constraintLayout)

                }
            }
        }

        searchInput.setOnFocusChangeListener { view, hasFocus ->

            if (hasFocus && searchInput.text.isEmpty()) {
                updateUIComposition("search_history")
            }

        }


        val btnClearInput: ImageButton = findViewById<ImageButton>(R.id.btnClearInput)
        btnClearInput.setOnClickListener {
            searchInput.text.clear()
            btnClearInput.isVisible = false
            // убираем клавиатуру
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchInput.windowToken, 0)
            searchInput.clearFocus()
            imgError.visibility = View.INVISIBLE
            textError.visibility = View.INVISIBLE
            tracks.clear()
            adapter.notifyDataSetChanged()
        }

        btnClearInput.visibility = View.INVISIBLE
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnClearInput.isVisible = !s.isNullOrEmpty()
                if (searchInput.hasFocus() && s?.isEmpty() == true) {
                    updateUIComposition("search_history")
                }
            }

            override fun afterTextChanged(s: Editable?) {
                globalSearchText = s?.toString() ?: ""
                println("")
            }
        }
        searchInput.addTextChangedListener(simpleTextWatcher)

        clear_history_button.setOnClickListener {
            searchHistory.clear()
            tracksHistory.clear()
            adapterSearchHistory.notifyDataSetChanged()
            updateUIComposition("search_result")

        }

        fun onApiError() {
            searchProgressBar.visibility = View.INVISIBLE
            tracks.clear()
            textError.text = getString(R.string.connection_issues)
            textError.visibility = View.VISIBLE
            imgError.setImageResource(R.drawable.ic_connection_issues)
            imgError.visibility = View.VISIBLE
            retryButton.visibility = View.VISIBLE
            recyclerView.adapter = TrackAdapter(tracks)
        }

        fun startApiSearch() {
            if (globalSearchText.isNotBlank()) {
                searchProgressBar.visibility = View.VISIBLE
                itunesService.search(searchInput.text.toString()).enqueue(object :
                    Callback<SearchResponse> {
                    override fun onResponse(
                        call: Call<SearchResponse>,
                        response: Response<SearchResponse>
                    ) {
                        if (response.code() == 200) {
                            tracks.clear()
                            retryButton.visibility = View.INVISIBLE
                            if (response.body()?.results?.isNotEmpty() == true) {
                                tracks.addAll(response.body()?.results!!)
                            }
                            if (tracks.isEmpty()) {
                                imgError.setImageResource(R.drawable.ic_no_results)
                                imgError.visibility = View.VISIBLE
                                textError.text = getString(R.string.nothing_was_found)
                                textError.visibility = View.VISIBLE
                                tracks.clear()
                            } else {
                                imgError.visibility = View.INVISIBLE
                                textError.visibility = View.INVISIBLE
                            }
                            searchProgressBar.visibility = View.INVISIBLE
                            adapter.notifyDataSetChanged()
                        } else {
                            onApiError()
                        }
                    }

                    override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                        onApiError()
                    }

                })
            }
        }
        searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                updateUIComposition("search_result")
                startApiSearch()
            }
            false
        }

        retryButton.setOnClickListener {
            startApiSearch()
        }

        adapter = TrackAdapter(tracks)

        searchInput.requestFocus()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("SearchText", globalSearchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        globalSearchText = savedInstanceState.getString("SearchText", "")
        searchInput.setText(globalSearchText)
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(prefsListener)
    }
}