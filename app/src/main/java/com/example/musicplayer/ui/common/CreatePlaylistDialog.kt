package com.example.musicplayer.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.musicplayer.R
import com.example.musicplayer.data.Playlist
import com.example.musicplayer.ui.MusicViewModel

@Composable
fun CreatePlaylistDialog(
    viewModel: MusicViewModel,
    modifier: Modifier = Modifier
) {
    var playlistNameInput by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var supportingText: Int? by remember { mutableStateOf(null) }

    Dialog(onDismissRequest = {
        viewModel.setShowDialogState(false)
    }, properties = DialogProperties(
        dismissOnBackPress = true,
        dismissOnClickOutside = true
    )) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(240.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.new_playlist_dialog_title),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(8.dp)
                )
                EditTextField(
                    label = R.string.playlist_name,
                    supportingText = supportingText,
                    isError = isError,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    value = playlistNameInput,
                    onValueChanged = { playlistNameInput = it }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(
                        onClick = {
                            viewModel.setShowDialogState(false)
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(id = R.string.cancel))
                    }
                    TextButton(
                        onClick = {
                            if (playlistNameInput.isBlank()) {
                                isError = true
                                supportingText = R.string.enter_playlist_name
                            } else if (playlistNameInput.length > 50) {
                                isError = true
                                supportingText = R.string.playlist_name_too_long
                            } else if (
                                viewModel.checkPlaylistNameUsed(playlistNameInput)
                                ) {
                                isError = true
                                supportingText = R.string.playlist_name_used
                            } else {
                                viewModel.addPlaylist(
                                    Playlist(playlistNameInput, mutableListOf())
                                )
                                viewModel.setShowDialogState(false)
                            }
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(id = R.string.create))
                    }
                }
            }
        }
    }
}