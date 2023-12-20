package com.practicum.playlistmaker.data.playlists


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.practicum.playlistmaker.data.convertors.PlaylistDbConvertor
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.playlists.db.entity.PlaylistEntity
import com.practicum.playlistmaker.domain.IMAGE_QUALITY
import com.practicum.playlistmaker.domain.PLAYLISTS_IMAGE_DIRECTORY
import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.domain.playlists.PlaylistsRepository
import com.practicum.playlistmaker.domain.playlists.model.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val convertor: PlaylistDbConvertor,
    private val context: Context
) : PlaylistsRepository {

    private val imagePath = File(
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        PLAYLISTS_IMAGE_DIRECTORY
    )

    init {
        if (!imagePath.exists()) {
            imagePath.mkdirs()
        }
    }

    override suspend fun addPlaylist(
        name: String,
        description: String,
        pictureUri: Uri?
    ) {
        appDatabase
            .playlistsDao()
            .addPlaylistEntity(
                convertor.map(
                    Playlist(
                        id = null,
                        name = name,
                        description = description,
                        pictureName = saveImageAndTakeName(pictureUri),
                        idsList = listOf(),
                        numbersOfTrack = 0
                    )
                )
            )
    }

    override suspend fun updatePlaylistById(
        idPlaylist: Int,
        name: String,
        description: String,
        pictureUri: Uri?
    ) {
        val oldPlaylist = getPlaylistById(idPlaylist)
        val newPictureName =
            if (pictureUri != null) {
                if (oldPlaylist.pictureName != null) {
                    deleteImageByName(oldPlaylist.pictureName)
                }
                saveImageAndTakeName(pictureUri)
            } else oldPlaylist.pictureName

        appDatabase.playlistsDao()
            .updatePlaylist(
                convertor.map(
                    oldPlaylist.copy(
                        id = idPlaylist,
                        name = name,
                        description = description,
                        pictureName = newPictureName,
                    )
                )
            )
    }

    override fun getAllPlaylist(): Flow<List<Playlist>> {
        return flow {
            val playlists = appDatabase
                .playlistsDao()
                .getAllPlaylist()
            emit(convertFromPlaylistEntity(playlists))
        }
    }

    private suspend fun isIdInAnyPlaylist(id: Int): Boolean {
        val playlists = getAllPlaylist().first()
        for (playlist in playlists) {
            if (id in playlist.idsList) {
                return true
            }
        }
        return false
    }

    override suspend fun deleteTrackFromPlaylistByIds(idTrack: Int, idPlaylist: Int) {
        val playlist = getPlaylistById(idPlaylist)
        val tracks = playlist.idsList.toMutableList()
        tracks.remove(idTrack)
        appDatabase.playlistsDao().replacePlaylistEntity(
            convertor.map(
                playlist.copy(
                    idsList = tracks.toList(),
                    numbersOfTrack = playlist.numbersOfTrack - 1
                )
            )
        )
        if (!isIdInAnyPlaylist(idTrack)) {
            appDatabase.playlistsDao().deleteTrackFromAllPlaylistById(idTrack)
        }
    }

    override suspend fun getPlaylistById(idPlaylist: Int): Playlist {
        return convertor.map(
            appDatabase.playlistsDao().getPlaylistById(idPlaylist)
        )
    }

    override suspend fun deletePlaylistById(idPlaylist: Int) {
        val playlist = getPlaylistById(idPlaylist)
        playlist.idsList.forEach { idTrack ->
            if (!isIdInAnyPlaylist(idTrack)) {
                appDatabase.playlistsDao().deleteTrackFromAllPlaylistById(idTrack)
            }
        }
        appDatabase.playlistsDao().deletePlaylistById(idPlaylist)
    }

    private fun saveImageAndTakeName(uri: Uri?): String? {
        if (uri == null) return null
        val imageName = System.currentTimeMillis().toString() + ".jpg"
        val file = File(imagePath, imageName)
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, outputStream)

        return imageName
    }

    private fun deleteImageByName(imageName: String) {
        if (File(imagePath, imageName).exists()) {
            File(imagePath, imageName).delete()
        }
    }

    override suspend fun addIdTrackToPlaylist(track: Track, playlist: Playlist) {
        val newListTrack = mutableListOf<Int>()
        newListTrack.apply {
            addAll(playlist.idsList)
            add(track.trackId)
        }
        val updatedPlaylist = playlist.copy(
            idsList = newListTrack.toList(),
            numbersOfTrack = playlist.numbersOfTrack + 1
        )
        appDatabase.playlistsDao().updatePlaylist(convertor.map(updatedPlaylist))
        appDatabase.playlistsDao().addTrackToPlaylists(convertor.map(track))
    }

    override fun getTracksByPlaylistId(playlistId: Int): Flow<List<Track>> {
        return flow {
            val playlist = convertor.map(
                appDatabase.playlistsDao().getPlaylistById(playlistId)
            )
            val tracks = mutableListOf<Track>()
            for (trackId in playlist.idsList) {
                val track = convertor.map(
                    appDatabase.playlistsDao().getTrackById(trackId)
                )
                tracks.add(track)
            }
            emit(tracks)
        }
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> convertor.map(playlist) }
    }
}