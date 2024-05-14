package com.example.musicplayer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.Playlist
import com.example.musicplayer.data.Track
import com.example.musicplayer.network.MusicAPI
import com.example.musicplayer.network.asDataModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MusicViewModel : ViewModel() {
    private var isIOException: Boolean = false
    private val homePlaylist = mutableListOf<Track>()
    private val favoritePlaylist = mutableListOf<Track>()
    private val playlists = mutableListOf<Playlist>()
    private val playlistNames = mutableSetOf<String>()
    private val selectedPlaylistTracks = mutableListOf<Track>()
    private lateinit var selectedTrack: Track
    private lateinit var selectedPlaylistName: String

    init {
        getMusicPlaylist()
    }

    fun getMusicPlaylist() {
        viewModelScope.launch {
            try {
                val networkPlaylist = MusicAPI.retrofitService.getPlaylist()
                homePlaylist.addAll(
                    networkPlaylist.music.asDataModel()
                )
                selectedPlaylistTracks.addAll(homePlaylist)
            } catch (e: IOException) {
                isIOException = true
                e.printStackTrace()
            } catch(e: HttpException) {
                isIOException = false
                e.printStackTrace()
            }
        }
    }

    fun checkIsIOException() = isIOException

    fun getHomePlaylist() = homePlaylist

    fun getFavoritePlaylist() = favoritePlaylist

    fun getPlaylists() = playlists

    fun selectTrack(track: Track) {
        selectedTrack = track
    }

    fun getSelectedTrack() = selectedTrack

    fun selectPlaylist(playlist: Playlist) {
        selectedPlaylistName = playlist.name
        selectPlaylistTracks(playlist.tracks)
    }

    fun selectPlaylistName(name: String) {
        selectedPlaylistName = name
    }

    fun getSelectedPlaylistName() = selectedPlaylistName

    fun selectPlaylistTracks(tracks: List<Track>) {
        if (tracks.isNotEmpty()) {
            if (selectedPlaylistTracks.isNotEmpty()) {
                selectedPlaylistTracks.clear()
            }
            selectedPlaylistTracks.addAll(tracks)
        }
    }

    fun getSelectedPlaylistTracks() = selectedPlaylistTracks

    fun getPreviousTrack(): Track? {
        if (selectedTrack.id == selectedPlaylistTracks.first().id) {
            return null
        } else {
            for (i in selectedPlaylistTracks.indices) {
                if (selectedTrack.id == selectedPlaylistTracks[i].id) {
                    selectedTrack = selectedPlaylistTracks[i - 1]
                    return selectedTrack
                }
            }
        }
        return null
    }

    fun getNextTrack(): Track? {
        if (selectedTrack.id == selectedPlaylistTracks.last().id) {
            return null
        } else {
            for (i in selectedPlaylistTracks.indices) {
                if (selectedTrack.id == selectedPlaylistTracks[i].id) {
                    selectedTrack = selectedPlaylistTracks[i + 1]
                    return selectedTrack
                }
            }
        }
        return null
    }

    fun addToSelectedPlaylist(track: Track) {
        for (item in playlists) {
            if (selectedPlaylistName == item.name) {
                item.tracks.add(track)
                break
            }
        }
    }

    fun deleteFromSelectedPLaylist(track: Track) {
        for (item in playlists) {
            if (selectedPlaylistName == item.name) {
                item.tracks.remove(track)
                break
            }
        }
    }

    fun changeFavoritePlaylist(trackToChange: Track) {
        if (trackToChange.isFavorite) {
            favoritePlaylist.add(trackToChange)
        } else {
            for(track in favoritePlaylist) {
                if (trackToChange.id == track.id) {
                    favoritePlaylist.remove(track)
                    break
                }
            }
        }
    }
}