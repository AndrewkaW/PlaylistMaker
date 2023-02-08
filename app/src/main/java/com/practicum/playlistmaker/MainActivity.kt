package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.button_search)
        val buttonSearchClickListener : View.OnClickListener = object : View.OnClickListener{
            override fun onClick(p0: View?) {
                Toast.makeText(this@MainActivity, "Ищу музыку...", Toast.LENGTH_LONG).show()
            }
        }
        buttonSearch.setOnClickListener(buttonSearchClickListener)

        val buttonMediaLibrary = findViewById<Button>(R.id.button_media_library)
        buttonMediaLibrary.setOnClickListener{
            Toast.makeText(this@MainActivity, "Смотрим медиатеку..", Toast.LENGTH_LONG).show()
        }
        val buttonSettings = findViewById<Button>(R.id.button_settings)
        buttonSettings.setOnClickListener{
            Toast.makeText(this@MainActivity, "Заходим в настройки..", Toast.LENGTH_LONG).show()
        }
    }
}