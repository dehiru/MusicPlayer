package com.example.musicplayer.ui.screens.playlists

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.musicplayer.R
import com.example.musicplayer.data.Playlist
import com.example.musicplayer.ui.MusicViewModel
import com.example.musicplayer.ui.common.CreatePlaylistDialog

const val text_max_length = 26

@Composable
fun PlaylistsScreen(
    viewModel: MusicViewModel,
    onPlaylistClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shouldShowDialog by viewModel.shouldShowDialog.collectAsState()
    if (shouldShowDialog) {
        CreatePlaylistDialog(viewModel)
    }
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row (
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.library_music),
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = stringResource(id = R.string.playlists),
                style = MaterialTheme.typography.headlineMedium
            )
            OutlinedButton(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(start = 32.dp),
                onClick = {
                    viewModel.setShowDialogState(true)
                }
            ) {
                Text(text = stringResource(id = R.string.new_playlist_button_text))
            }
        }
        if (viewModel.getPlaylists().isEmpty()) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.no_playlists),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        } else {
            LazyColumn(
                modifier = modifier.padding(vertical = 8.dp),
            ) {
                items(
                    items = viewModel.getPlaylists(),
                    key = { playlist -> playlist.name}) { playlist ->
                    PlaylistCard(
                        playlist = playlist,
                        onPlaylistClicked,
                        viewModel,
                        modifier = modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun PlaylistCard(
    playlist: Playlist,
    onPlaylistClicked: () -> Unit,
    viewModel: MusicViewModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable {
                viewModel.selectPlaylist(playlist)
                onPlaylistClicked()
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
            Row(horizontalArrangement = Arrangement.Start) {
                Text(
                    text = if (playlist.name.length > text_max_length) {
                        playlist.name.take(text_max_length) + "..."
                    } else playlist.name,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = playlist.tracks.size.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.music_note),
                    contentDescription = null,
                    modifier = Modifier
                )
            }
        }
    }
}