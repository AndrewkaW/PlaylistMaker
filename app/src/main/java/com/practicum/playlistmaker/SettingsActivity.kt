package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //реализация кнопки назад на тулбаре
        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_id).setNavigationOnClickListener {
            finish()
        }

        //Реадизация кнопки «Поделиться приложением»
        findViewById<TextView>(R.id.share_app_butt).setOnClickListener{
            Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT,getString(R.string.url_android_dev))
                type = "text/plain"
                startActivity(this)
            }
        }

        //Реализация кнопик «Написать в поддержку»
        findViewById<TextView>(R.id.support_butt).setOnClickListener{
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
                putExtra(Intent.EXTRA_SUBJECT,getString(R.string.support_theme_massage))
                putExtra(Intent.EXTRA_TEXT,getString(R.string.support_massage))
                startActivity(this)
            }
        }

        //Реализация кнопки «Пользовательское соглашение»
        findViewById<TextView>(R.id.user_agreement_butt).setOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(getString(R.string.url_user_agreement))))
        }
        //Переключение темы по свичеру
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.dark_theme_switch)
        themeSwitcher.isChecked = (applicationContext as App).darkTheme
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }

    }

}