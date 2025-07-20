package com.saturnnetwork.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SearchActivity : AppCompatActivity() {

    private var globalSearchText: String = ""
    private lateinit var searchInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val btnBackFromSearch = findViewById<Button>(R.id.btnBackFromSearch)
        btnBackFromSearch.setOnClickListener {
            finish()
        }

        searchInput = findViewById<EditText>(R.id.searchInput)
        searchInput.requestFocus()

        val btnClearInput: ImageButton = findViewById<ImageButton>(R.id.btnClearInput)
        btnClearInput.setOnClickListener {
            searchInput.text.clear()
            btnClearInput.visibility = View.INVISIBLE
            // убираем клавиатуру
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchInput.windowToken, 0)
            searchInput.clearFocus()

        }

        btnClearInput.visibility = View.INVISIBLE
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {

                } else {
                    btnClearInput.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                globalSearchText = s?.toString() ?: ""
            }
        }
        searchInput.addTextChangedListener(simpleTextWatcher)
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
}