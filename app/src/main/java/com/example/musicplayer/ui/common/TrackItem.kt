package com.example.musicplayer.ui.common

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.musicplayer.R
import com.example.musicplayer.data.Track
import com.example.musicplayer.ui.MusicViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackItem(
    track: Track,
    onTrackClicked: () -> Unit,
    viewModel: MusicViewModel,
    modifier: Modifier = Modifier
) {
    var isFavorite by remember { mutableStateOf(track.isFavorite) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val waitWhileToastShowing: () -> Unit = {
        coroutineScope.launch {
            delay(3500)
            showBottomSheet = false
        }
    }

    if (showBottomSheet) {
        if (viewModel.getPlaylistNamesWithoutTrack().isEmpty()) {
            Toast.makeText(
                LocalContext.current,
                stringResource(R.string.track_in_all_playlists),
                Toast.LENGTH_LONG
            ).show()
            waitWhileToastShowing()
        } else {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                Column(
                    modifier = modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.select_playlist),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    LazyColumn(
                        modifier = modifier.padding(vertical = 8.dp),
                    ) {
                        items(
                            items = viewModel.getPlaylistNamesWithoutTrack(),
                            key = { playlistName -> playlistName}) { playlistName ->
                            PlaylistNameCard(
                                playlistName = playlistName,
                                onPlaylistNameClicked = {
                                    viewModel.selectPlaylistName(playlistName)
                                    viewModel.addToSelectedPlaylist(track)
                                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showBottomSheet = false
                                        }
                                    }
                                },
                                modifier = modifier
                                    .padding(8.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
                    TextButton(
                        onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        },
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Text(text = stringResource(R.string.cancel))
                    }
                }
            }
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                viewModel.selectTrack(track)
                onTrackClicked()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(track.image)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .padding(end = 16.dp)
        )
        Column {
            Row(
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(end = 8.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (track.title.length > text_max_length) {
                            track.title.take(text_max_length) + "..."
                        } else track.title,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = if (track.artist.length > text_max_length) {
                            track.artist.take(text_max_length) + "..."
                        } else track.artist,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = {
                            isFavorite = !isFavorite
                            track.isFavorite = isFavorite
                            viewModel.changeFavoritePlaylist(track)
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) {
                                Icons.Filled.Favorite
                            } else Icons.Outlined.FavoriteBorder,
                            contentDescription = null
                        )
                    }
                    IconButton(
                        onClick = {
                            viewModel.setPlaylistNamesWithoutTrack(track)
                            showBottomSheet = true
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.playlist_add),
                            contentDescription = null
                        )
                    }
                }
            }
            Divider()
        }
    }
}

@Composable
fun PlaylistNameCard(
    playlistName: String,
    onPlaylistNameClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable {
            onPlaylistNameClicked()
        },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (playlistName.length > text_max_length) {
                    playlistName.take(text_max_length) + "..."
                } else playlistName,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}