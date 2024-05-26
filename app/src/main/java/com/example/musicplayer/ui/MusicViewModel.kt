package com.example.musicplayer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.Playlist
import com.example.musicplayer.data.Track
import com.example.musicplayer.network.MusicAPI
import com.example.musicplayer.network.asDataModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MusicViewModel : ViewModel() {
    private var isIOException: Boolean = false
    private val homePlaylist = mutableListOf<Track>()
    private val favoritePlaylist = mutableListOf<Track>()
    private val playlists = mutableListOf<Playlist>()
    private val playlistNames = mutableSetOf<String>()
    private val playlistNamesWithoutTrack = mutableListOf<String>()
    private val selectedPlaylistTracks = mutableListOf<Track>()
    private lateinit var selectedTrack: Track
    private lateinit var selectedPlaylistName: String

    private val _shouldShowDialog = MutableStateFlow(false)
    val shouldShowDialog: StateFlow<Boolean> = _shouldShowDialog.asStateFlow()

    private val _updatedPlaylist = MutableStateFlow(mutableListOf<Track>())
    val updatedPlaylist: StateFlow<List<Track>> = _updatedPlaylist.asStateFlow()

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

    fun setShowDialogState(state: Boolean) {
        _shouldShowDialog.value = state
    }

    fun updatePlaylistState() {
        _updatedPlaylist.value = selectedPlaylistTracks
    }

    fun getHomePlaylist() = homePlaylist

    fun getFavoritePlaylist() = favoritePlaylist

    fun addPlaylist(playlist: Playlist) {
        playlistNames.add(playlist.name)
        playlists.add(playlist)
    }

    fun deleteSelectedPlaylist() {
        playlistNames.remove(selectedPlaylistName)
        for (track in homePlaylist){
            if (
                track.containedInPlaylists.contains(selectedPlaylistName)
            ) track.containedInPlaylists.remove(selectedPlaylistName)
        }
        for (playlist in playlists) {
            if (selectedPlaylistName == playlist.name) {
                playlists.remove(playlist)
                break
            }
        }
    }

    fun getPlaylists() = playlists

    fun checkPlaylistNameUsed(playlistName: String): Boolean {
        return playlistNames.contains(playlistName)
    }

    fun setPlaylistNamesWithoutTrack(track: Track) {
        if (playlistNamesWithoutTrack.isNotEmpty()) {
            playlistNamesWithoutTrack.clear()
        }
        for (playlist in playlists) {
            if (!track.containedInPlaylists.contains(playlist.name)) {
                playlistNamesWithoutTrack.add(playlist.name)
            }
        }
    }

    fun getPlaylistNamesWithoutTrack() = playlistNamesWithoutTrack

    fun selectTrack(track: Track) {
        selectedTrack = track
    }

    fun getSelectedTrack() = selectedTrack

    fun selectPlaylist(playlist: Playlist) {
        selectPlaylistName(playlist.name)
        selectPlaylistTracks(playlist.tracks)
        updatePlaylistState()
    }

    fun selectPlaylistName(name: String) {
        selectedPlaylistName = name
    }

    fun getSelectedPlaylistName() = selectedPlaylistName

    fun selectPlaylistTracks(tracks: List<Track>) {
        if (selectedPlaylistTracks.isNotEmpty()) {
            selectedPlaylistTracks.clear()
        }
        if (tracks.isNotEmpty()) {
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
        track.containedInPlaylists.add(selectedPlaylistName)
    }

    fun deleteFromSelectedPlaylist(track: Track) {
        for (item in playlists) {
            if (selectedPlaylistName == item.name) {
                item.tracks.remove(track)
                break
            }
        }
        track.containedInPlaylists.remove(selectedPlaylistName)
        updatePlaylistState()
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