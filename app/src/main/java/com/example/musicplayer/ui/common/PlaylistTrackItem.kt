package com.example.musicplayer.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.musicplayer.R
import com.example.musicplayer.data.Track
import com.example.musicplayer.ui.MusicViewModel

@Composable
fun PlaylistTrackItem(
    track: Track,
    onTrackClicked: () -> Unit,
    viewModel: MusicViewModel,
    modifier: Modifier = Modifier
) {
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
                            viewModel.deleteFromSelectedPlaylist(track)
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.delete),
                            contentDescription = null
                        )
                    }
                }
            }
            Divider()
        }
    }
}