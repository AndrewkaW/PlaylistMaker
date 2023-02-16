package com.practicum.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.button_search)
        buttonSearch.setOnClickListener{
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }


        val buttonMediaLibrary = findViewById<Button>(R.id.button_media_library)
        buttonMediaLibrary.setOnClickListener{
            val mediaLibraryIntent = Intent(this, MediaLibraryActivity::class.java)
            startActivity(mediaLibraryIntent)
        }
        val buttonSettings = findViewById<Button>(R.id.button_settings)
        buttonSettings.setOnClickListener{
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}