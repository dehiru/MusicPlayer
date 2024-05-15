package com.example.musicplayer.ui.screens

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.musicplayer.R
import com.example.musicplayer.ui.MusicViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TrackPlayer(
    viewModel: MusicViewModel,
    modifier: Modifier = Modifier
) {
    val mediaPlayer by remember { mutableStateOf(MediaPlayer()) }
    var track by remember { mutableStateOf(viewModel.getSelectedTrack()) }
    var isPlaying by remember { mutableStateOf(true) }
    var needChangeSource by remember { mutableStateOf(true) }
    var currentTime by remember { mutableIntStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
    val updateCurrentTime: () -> Unit = {
        coroutineScope.launch {
            delay(1000)
            currentTime = mediaPlayer.currentPosition
        }
    }

    if (needChangeSource) {
        needChangeSource = false
        val trackUrl = track.source
        try {
            mediaPlayer.setDataSource(trackUrl)
            mediaPlayer.prepareAsync()
        } catch(e: Exception) {
            isPlaying = false
            e.printStackTrace()
        }
    }
    mediaPlayer.setOnPreparedListener {
        if (isPlaying) {
            mediaPlayer.start()
            updateCurrentTime()
        }
        currentTime = mediaPlayer.currentPosition
    }
    if(isPlaying) {
        updateCurrentTime()
    }
    mediaPlayer.setOnCompletionListener {
        val next = viewModel.getNextTrack()
        if (next != null) {
            mediaPlayer.reset()
            track = next
            needChangeSource = true
        } else {
            mediaPlayer.seekTo(0)
            isPlaying = false
        }
    }
    DisposableEffect(key1 = LocalLifecycleOwner.current) {
        onDispose { mediaPlayer.release() }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(track.image)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(360.dp)
                .clip(RoundedCornerShape(8.dp))
                .padding(16.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = track.title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = track.artist,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Slider(
            value = (currentTime / 1000).toFloat(),
            onValueChange = {
                currentTime = (it * 1000).toInt()
            },
            onValueChangeFinished = {
                mediaPlayer.seekTo(currentTime)
            },
            valueRange = 0f..track.duration.toFloat(),
            modifier = Modifier.padding(start = 8.dp, end = 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = convertSecondsTime(currentTime / 1000),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = convertSecondsTime(track.duration),
                style = MaterialTheme.typography.labelMedium
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    if (mediaPlayer.currentPosition < 3000) {
                        val previous = viewModel.getPreviousTrack()
                        if (previous != null) {
                            mediaPlayer.reset()
                            track = previous
                            needChangeSource = true
                        } else {
                            mediaPlayer.seekTo(0)
                        }
                    } else {
                        mediaPlayer.seekTo(0)
                    }
                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.previous),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )
            }
            IconButton(
                onClick = {
                    if (isPlaying) {
                        mediaPlayer.pause()
                    } else mediaPlayer.start()
                    isPlaying = !isPlaying
                },
            ) {
                Icon(
                    imageVector = if(isPlaying) {
                        ImageVector.vectorResource(R.drawable.pause)
                    } else ImageVector.vectorResource(R.drawable.play),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )
            }
            IconButton(
                onClick = {
                    val next = viewModel.getNextTrack()
                    if (next != null) {
                        mediaPlayer.reset()
                        track = next
                        needChangeSource = true
                    }
                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.next),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )
            }

        }
    }
}

fun convertSecondsTime(secondsReceived: Int): String {
    val minutes = secondsReceived / 60
    val seconds = secondsReceived % 60
    if (seconds < 10) return "$minutes:0$seconds"
    return "$minutes:$seconds"
}