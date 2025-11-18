package com.saturnnetwork.playlistmaker.search.ui


import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.saturnnetwork.playlistmaker.R
import com.saturnnetwork.playlistmaker.databinding.ActivitySearchBinding
import com.saturnnetwork.playlistmaker.search.domain.models.Track
import com.saturnnetwork.playlistmaker.utils.gone
import com.saturnnetwork.playlistmaker.utils.hide
import com.saturnnetwork.playlistmaker.utils.show
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SearchActivity : AppCompatActivity() {

    private var globalSearchText: String = ""
    private lateinit var adapter: TrackAdapter
    private lateinit var adapterSearchHistory: TrackAdapter
    private var suppressUIUpdate = false
    private var blockTextWatcher = false


    companion object {
        private const val SEARCH_DELAY_MILLIS = 2000L
    }

    private val handler = Handler(Looper.getMainLooper())

    private lateinit var binding: ActivitySearchBinding

    private val viewModel: SearchViewModel by viewModel()

    private fun showLoading() {
        binding.searchProgressBar.show()
        listOf(binding.youSearched,
            binding.textError,
            binding.imgError,
            binding.tracksRecyclerView,
            binding.clearHistoryButton,
            binding.retryButton).hide()
    }

    private fun showError(state: SearchState, error: String) {
        updateUIComposition(state.composition, state.tracks, error)
    }

    private fun showContent(tracks: ArrayList<Track>, composition: String) {
        updateUIComposition(composition, tracks, null)
    }

    fun updateUIComposition(composition: String, tracks: ArrayList<Track>, error: String?) {
        when (composition) {
            "history" -> {
                listOf(binding.searchProgressBar,binding.textError, binding.imgError).hide()
                listOf(binding.youSearched, binding.tracksRecyclerView, binding.clearHistoryButton).show()
                val constraintSet = ConstraintSet()
                val constraintLayout = findViewById<ConstraintLayout>(R.id.activity_search)
                constraintSet.clone(constraintLayout)
                val buttonHeight = binding.clearHistoryButton.height
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

                val params = binding.tracksRecyclerView.layoutParams as ConstraintLayout.LayoutParams
                params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                binding.tracksRecyclerView.layoutParams = params
                binding.tracksRecyclerView.isNestedScrollingEnabled = true


                adapterSearchHistory = TrackAdapter(tracks, viewModel.getInteractor())
                binding.tracksRecyclerView.adapter = adapterSearchHistory

                // Проверка, вышел ли RecyclerView за границу
                binding.tracksRecyclerView.post {
                    val layoutManager = binding.tracksRecyclerView.layoutManager as LinearLayoutManager
                    val lastPos = adapterSearchHistory.itemCount - 1
                    val lastView = layoutManager.findViewByPosition(lastPos)

                    if (lastView != null) {
                        val viewLocation = IntArray(2)
                        lastView.getLocationOnScreen(viewLocation)
                        val lastViewBottom = viewLocation[1] + lastView.height

                        val navBarHeight = ViewCompat.getRootWindowInsets(binding.tracksRecyclerView)
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

                            val params = binding.tracksRecyclerView.layoutParams as ConstraintLayout.LayoutParams
                            params.height = 0
                            params.matchConstraintMaxHeight = Int.MAX_VALUE
                            binding.tracksRecyclerView.layoutParams = params
                            binding.tracksRecyclerView.isNestedScrollingEnabled = true

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

            "search_result" -> {
                listOf(binding.searchProgressBar, binding.textError, binding.retryButton,
                    binding.imgError).hide()
                binding.tracksRecyclerView.show()

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

                val params = binding.tracksRecyclerView.layoutParams as ConstraintLayout.LayoutParams
                params.height = 0
                params.matchConstraintMaxHeight = Int.MAX_VALUE
                binding.tracksRecyclerView.layoutParams = params
                binding.tracksRecyclerView.isNestedScrollingEnabled = true

                adapter = TrackAdapter(tracks, viewModel.getInteractor())
                binding.tracksRecyclerView.adapter = adapter

            }
            "error" -> {
                listOf(binding.searchProgressBar, binding.clearHistoryButton, binding.youSearched).hide()
                listOf(binding.textError, binding.imgError).show()
                adapter = TrackAdapter(tracks, viewModel.getInteractor())
                binding.tracksRecyclerView.adapter = adapter
                binding.textError.text = error
                if (error == getString(R.string.nothing_was_found)) {
                    binding.imgError.setImageResource(R.drawable.ic_no_results)
                    binding.retryButton.hide()
                } else {
                    binding.imgError.setImageResource(R.drawable.ic_connection_issues)
                    binding.retryButton.show()
                }

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // восстановим текст из ViewModel
        binding.searchInput.setText(viewModel.getSearchText())
        binding.tracksRecyclerView.layoutManager = LinearLayoutManager(this)
        listOf(binding.searchProgressBar, binding.textError, binding.retryButton,
            binding.imgError, binding.btnClearInput, binding.tracksRecyclerView, binding.youSearched).hide()

        viewModel.observeSearchStateLiveData().observe(this) { state ->
            when {
                state.isLoading -> showLoading()
                state.errorMessageRes != null -> showError(state,getString(state.errorMessageRes))
                else -> showContent(state.tracks, state.composition)
            }

        }

        binding.btnBackFromSearch.setOnClickListener {
            finish()
        }

        binding.searchInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.searchInput.text.isEmpty()) {
                /* Показываем историю только если ранее не было результатов поиска,
                Когда я снова подписываюсь на LiveData через observe при пересоздании
                экрана при его повороте, LiveData немедленно отдаёт последнее значение новому Observer.
                То есть даже если Activity была уничтожена, LiveData всё равно «помнит» последний результат поиска.
                 */
                val state = viewModel.observeSearchStateLiveData().value
                if (state != null && !state.tracks.isEmpty) {
                    when {
                        state.isLoading -> showLoading()
                        state.errorMessageRes != null -> showError(state,getString(state.errorMessageRes))
                        else -> showContent(state.tracks, state.composition)
                    }
                } else {
                    viewModel.loadFromHistory()
                }
            }
        }


        binding.btnClearInput.setOnClickListener {
            suppressUIUpdate = true
            binding.searchInput.text.clear()
            binding.btnClearInput.hide()
            globalSearchText = ""
            viewModel.searchTracks(globalSearchText.toString())
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.searchInput.windowToken, 0)
            binding.searchInput.clearFocus()
            binding.searchInput.post {
                suppressUIUpdate = false
            }
        }



        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
        }

        val searchRunnable = Runnable {
            if (globalSearchText.isNotBlank()) {
                viewModel.searchTracks(globalSearchText.toString()) }
            }

        fun searchDebounce() {
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DELAY_MILLIS)
        }


        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (suppressUIUpdate || blockTextWatcher) return
                binding.btnClearInput.isVisible = !s.isNullOrEmpty()
                if (!suppressUIUpdate && binding.searchInput.hasFocus() && s?.isEmpty() == true) {
                    viewModel.loadFromHistory()
                } else {
                    searchDebounce()
                }

            }

            override fun afterTextChanged(s: Editable?) {
                val text = s?.toString().orEmpty()
                globalSearchText = text
                viewModel.setSearchText(text)
            }
        }

        binding.searchInput.addTextChangedListener(simpleTextWatcher)
        binding.searchInput.requestFocus()

        binding.retryButton.setOnClickListener {
            viewModel.searchTracks(globalSearchText)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }



    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

}