package com.example.musicplayer.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.musicplayer.R
import com.example.musicplayer.ui.MusicViewModel
import com.example.musicplayer.ui.common.TrackItem

@Composable
fun HomeScreen(
    viewModel: MusicViewModel,
    onTrackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (viewModel.getHomePlaylist().isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (viewModel.checkIsIOException()) {
                    stringResource(id = R.string.unable_to_access_network)
                } else stringResource(id = R.string.error_403_forbidden),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    } else {
        LazyColumn(
            modifier = modifier.padding(vertical = 4.dp),
        ) {
            items(
                items = viewModel.getHomePlaylist(),
                key = { track -> track.id}) { track ->
                TrackItem(
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