package com.practicum.playlistmaker.data.sharing.impl

import android.content.Context
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.sharing.SharingStorage
import com.practicum.playlistmaker.domain.sharing.model.EmailData

class SharingStorageImpl(private val context: Context) : SharingStorage {

    override fun getShareAppLink(): String {
        return context.getString(R.string.url_android_dev)
    }

    override fun getTermsLink(): String {
        return context.getString(R.string.url_user_agreement)
    }

    override fun getSupportEmailData(): EmailData {
        return EmailData(
            email = context.getString(R.string.support_email),
            subject = context.getString(R.string.support_theme_massage),
            text = context.getString(R.string.support_massage),
        )
    }
}