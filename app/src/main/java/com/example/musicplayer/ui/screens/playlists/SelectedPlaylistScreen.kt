package com.example.musicplayer.ui.screens.playlists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.musicplayer.ui.MusicViewModel
import com.example.musicplayer.ui.common.PlaylistTrackItem

const val playlist_name_max_length = 14
@Composable
fun SelectedPlaylistScreen(
    viewModel: MusicViewModel,
    onArrowBackClicked: () -> Unit,
    onTrackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val updatedPlaylist by viewModel.updatedPlaylist.collectAsState()
    val playlistName = viewModel.getSelectedPlaylistName()

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onArrowBackClicked,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.arrow_back),
                        contentDescription = null
                    )
                }
                Text(
                    text = if (playlistName.length > playlist_name_max_length) {
                        playlistName.take(playlist_name_max_length) + "..."
                    } else playlistName,
                    style = MaterialTheme.typography.headlineMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        viewModel.deleteSelectedPlaylist()
                        onArrowBackClicked()
                    },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.delete),
                        contentDescription = null
                    )
                }
            }
        }
        if (viewModel.getSelectedPlaylistTracks().isEmpty()) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.playlist_empty),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        } else {
            LazyColumn(
                modifier = modifier.padding(vertical = 4.dp),
            ) {
                items(
                    //items = viewModel.getSelectedPlaylistTracks(),
                    items = updatedPlaylist,
                    key = { track -> track.id}) { track ->
                    PlaylistTrackItem(
                        track = track,
                        onTrackClicked,
                        viewModel,
                        modifier = modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}