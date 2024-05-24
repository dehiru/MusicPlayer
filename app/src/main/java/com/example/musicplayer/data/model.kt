package com.example.musicplayer.data

data class Track(
    val id: String,
    val title: String,
    val artist: String,
    val source: String,
    val image: String,
    val duration: Int,
    var isFavorite: Boolean = false,
    val containedInPlaylists: MutableSet<String> = mutableSetOf()
)

data class Playlist(
    var name: String,
    val tracks: MutableList<Track>
)