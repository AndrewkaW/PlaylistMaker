package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //реализация кнопки назад на тулбаре
        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_id).setOnClickListener {
            finish()
        }

        //Реадизация кнопки «Поделиться приложением»
        findViewById<TextView>(R.id.share_app_butt).setOnClickListener{
            val shareAppIntent = Intent(Intent.ACTION_SEND)
            shareAppIntent.putExtra(Intent.EXTRA_TEXT,getString(R.string.url_android_dev))
            shareAppIntent.type = "text/plain"
            startActivity(shareAppIntent)
        }

        //Реализация кнопик «Написать в поддержку»
        findViewById<TextView>(R.id.support_butt).setOnClickListener{
            val toSupportIntent = Intent(Intent.ACTION_SENDTO)
            toSupportIntent.data = Uri.parse("mailto:")
            toSupportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
            toSupportIntent.putExtra(Intent.EXTRA_TEXT,getString(R.string.support_massage))
            startActivity(toSupportIntent)
        }

        //Реализация кнопки «Пользовательское соглашение»
        findViewById<TextView>(R.id.user_agreement_butt).setOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(getString(R.string.url_user_agreement))))
        }
    }
}