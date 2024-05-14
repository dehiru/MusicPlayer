package com.example.musicplayer.network

import com.example.musicplayer.data.Track
import kotlinx.serialization.Serializable

@Serializable
data class NetworkPlaylist(
    val music: List<NetworkTrack>
)

@Serializable
data class NetworkTrack(
    val id: String,
    val title: String,
    val album: String,
    val artist: String,
    val genre: String,
    val source: String,
    val image: String,
    val trackNumber: Int,
    val totalTrackCount: Int,
    val duration: Int, // SECONDS
    val site: String
)

fun List<NetworkTrack>.asDataModel(): List<Track> {
    return map {
        Track(
            id = it.id,
            title = it.title,
            artist = it.artist,
            source = it.source,
            image = it.image,
            duration = it.duration
        )
    }
}