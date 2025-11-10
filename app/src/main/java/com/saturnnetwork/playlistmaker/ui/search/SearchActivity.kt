package com.saturnnetwork.playlistmaker.ui.search

import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saturnnetwork.playlistmaker.R
import com.saturnnetwork.playlistmaker.di.TracksInteractorCreator
import com.saturnnetwork.playlistmaker.domain.TracksInteractor
import com.saturnnetwork.playlistmaker.domain.models.Track


class SearchActivity : AppCompatActivity() {

    private var globalSearchText: String = ""
    private lateinit var searchInput: EditText

    private lateinit var imgError: ImageView
    private lateinit var textError: TextView
    private lateinit var retryButton: Button

    private lateinit var clear_history_button: Button

    private lateinit var youSearched: TextView

    private lateinit var searchProgressBar: ProgressBar

    private var tracks: ArrayList<Track> = ArrayList()

    private lateinit var sharedPrefs: SharedPreferences

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TrackAdapter
    private lateinit var adapterSearchHistory: TrackAdapter

    private var tracksHistory: ArrayList<Track> = ArrayList()

    private var suppressUIUpdate = false

    companion object {
        private const val SEARCH_DELAY_MILLIS = 2000L
    }

    private val prefsListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        when (key) {
            "prefsHistory" -> {
                val interactor = TracksInteractorCreator.create(sharedPrefs)
                tracksHistory = interactor.loadFromHistory()
                Handler(Looper.getMainLooper()).post {
                    adapterSearchHistory.notifyDataSetChanged()
                }

            }
        }
    }


    private val handler = Handler(Looper.getMainLooper())




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        sharedPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        sharedPrefs.registerOnSharedPreferenceChangeListener(prefsListener)
        val interactor = TracksInteractorCreator.create(sharedPrefs)
        adapterSearchHistory = TrackAdapter(tracksHistory, interactor)

        tracksHistory = interactor.loadFromHistory()

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

                        val buttonHeight = clear_history_button.height
                        val margin = (24 * resources.displayMetrics.density).toInt()
                        val maxHeight = buttonHeight + margin

                        constraintSet.connect(
                            R.id.tracksRecyclerView,
                            ConstraintSet.TOP,
                            R.id.youSearched,
                            ConstraintSet.BOTTOM,
                            (8 * resources.displayMetrics.density).toInt()
                        )

                        constraintSet.clear(R.id.tracksRecyclerView, ConstraintSet.BOTTOM)

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

                        val params = recyclerView.layoutParams as ConstraintLayout.LayoutParams
                        params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                        recyclerView.layoutParams = params
                        recyclerView.isNestedScrollingEnabled = true


                        adapterSearchHistory = TrackAdapter(tracksHistory, interactor)
                        recyclerView.adapter = adapterSearchHistory

                        // Проверка, вышел ли RecyclerView за границу
                        recyclerView.post {
                            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                            val lastPos = adapterSearchHistory.itemCount - 1
                            val lastView = layoutManager.findViewByPosition(lastPos)

                            if (lastView != null) {
                                val viewLocation = IntArray(2)
                                lastView.getLocationOnScreen(viewLocation)
                                val lastViewBottom = viewLocation[1] + lastView.height

                                val navBarHeight = ViewCompat.getRootWindowInsets(recyclerView)
                                    ?.getInsets(WindowInsetsCompat.Type.navigationBars())
                                    ?.bottom ?: 0

                                val screenHeight = Resources.getSystem().displayMetrics.heightPixels
                                val safeScreenHeight = screenHeight - navBarHeight

                                val isLastItemVisible = lastViewBottom <= safeScreenHeight

                                if (!isLastItemVisible) {
                                    constraintSet.clear(R.id.clear_history_button, ConstraintSet.TOP)
                                    constraintSet.connect(
                                        R.id.clear_history_button,
                                        ConstraintSet.BOTTOM,
                                        ConstraintSet.PARENT_ID,
                                        ConstraintSet.BOTTOM,
                                        (24 * resources.displayMetrics.density).toInt()
                                    )
                                    constraintSet.connect(
                                        R.id.tracksRecyclerView,
                                        ConstraintSet.BOTTOM,
                                        R.id.clear_history_button,
                                        ConstraintSet.TOP,
                                        (24 * resources.displayMetrics.density).toInt()
                                    )

                                    val params = recyclerView.layoutParams as ConstraintLayout.LayoutParams
                                    params.height = 0
                                    params.matchConstraintMaxHeight = Int.MAX_VALUE
                                    recyclerView.layoutParams = params
                                    recyclerView.isNestedScrollingEnabled = true

                                } else {
                                    constraintSet.clear(R.id.clear_history_button, ConstraintSet.BOTTOM)
                                    constraintSet.connect(
                                        R.id.clear_history_button,
                                        ConstraintSet.TOP,
                                        R.id.tracksRecyclerView,
                                        ConstraintSet.BOTTOM,
                                        (24 * resources.displayMetrics.density).toInt()
                                    )
                                }

                                constraintSet.applyTo(constraintLayout)
                            }
                        }

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

                    constraintSet.applyTo(constraintLayout)

                    val params = recyclerView.layoutParams as ConstraintLayout.LayoutParams
                    params.height = 0
                    params.matchConstraintMaxHeight = Int.MAX_VALUE
                    recyclerView.layoutParams = params
                    recyclerView.isNestedScrollingEnabled = true

                    recyclerView.adapter = adapter

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

            suppressUIUpdate = true

            searchInput.text.clear()
            btnClearInput.isVisible = false

            updateUIComposition("search_result")

            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchInput.windowToken, 0)
            searchInput.clearFocus()

            imgError.visibility = View.INVISIBLE
            textError.visibility = View.INVISIBLE
            retryButton.visibility = View.INVISIBLE

            tracks.clear()
            adapter.notifyDataSetChanged()

            searchInput.post {
                suppressUIUpdate = false
            }
        }

        btnClearInput.visibility = View.INVISIBLE


        clear_history_button.setOnClickListener {
            interactor.clearHistory()
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
            recyclerView.adapter = TrackAdapter(tracks, interactor)
        }

        fun startApiSearch() {
            if (globalSearchText.isNotBlank()) {
                // Вызываем поиск песен
                searchProgressBar.visibility = View.VISIBLE
                interactor.searchTracks(
                    searchInput.text.toString(),
                    object : TracksInteractor.TracksConsumer {
                        override fun consume(foundTracks: ArrayList<Track>) {
                            runOnUiThread {
                                if (foundTracks.isNotEmpty()) {
                                    imgError.visibility = View.INVISIBLE
                                    textError.visibility = View.INVISIBLE
                                    tracks.clear()
                                    tracks.addAll(foundTracks)
                                } else {
                                    imgError.setImageResource(R.drawable.ic_no_results)
                                    imgError.visibility = View.VISIBLE
                                    textError.text = getString(R.string.nothing_was_found)
                                    textError.visibility = View.VISIBLE
                                    tracks.clear()
                                }
                                searchProgressBar.visibility = View.INVISIBLE
                                adapter.notifyDataSetChanged()
                            }
                        }
                        override fun onError() {
                            runOnUiThread {
                                onApiError()
                            }
                        }
                    })
            }
        }

        fun searchRequest() {
            startApiSearch()
        }

        val searchRunnable = Runnable { searchRequest() }

        fun searchDebounce() {
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DELAY_MILLIS)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnClearInput.isVisible = !s.isNullOrEmpty()
                if (!suppressUIUpdate && searchInput.hasFocus() && s?.isEmpty() == true) {
                    updateUIComposition("search_history")
                } else {
                    searchDebounce()
                    updateUIComposition("search_result")
                }

            }

            override fun afterTextChanged(s: Editable?) {
                globalSearchText = s?.toString() ?: ""
            }
        }

        searchInput.addTextChangedListener(simpleTextWatcher)

        // old method
        /*searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                updateUIComposition("search_result")
                startApiSearch()
            }
            false
        }*/

        retryButton.setOnClickListener {
            startApiSearch()
        }

        adapter = TrackAdapter(tracks, interactor)

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
        handler.removeCallbacksAndMessages(null)
    }

}