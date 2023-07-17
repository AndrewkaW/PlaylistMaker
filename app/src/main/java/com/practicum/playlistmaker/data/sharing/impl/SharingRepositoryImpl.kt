package com.practicum.playlistmaker.data.sharing.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.data.sharing.SharingStorage
import com.practicum.playlistmaker.domain.sharing.SharingRepository

class SharingRepositoryImpl(
    private val context: Context,
    private val storage: SharingStorage
) : SharingRepository {

    companion object {
        const val TYPE_SHARE_LINK = "text/plain"
        const val EMAIL_URI = "mailto:"
    }

    override fun shareLink() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, storage.getShareAppLink())
            type = TYPE_SHARE_LINK
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun openLink() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(storage.getTermsLink())
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun openEmail() {
        val supportEmailData = storage.getSupportEmailData()

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(EMAIL_URI)
            putExtra(Intent.EXTRA_EMAIL, arrayOf(supportEmailData.email))
            putExtra(Intent.EXTRA_SUBJECT, supportEmailData.subject)
            putExtra(Intent.EXTRA_TEXT, supportEmailData.text)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}