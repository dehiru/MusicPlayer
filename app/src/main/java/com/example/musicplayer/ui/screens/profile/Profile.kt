package com.example.musicplayer.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.musicplayer.R
import com.example.musicplayer.data.PreferencesManager
import com.example.musicplayer.ui.theme.MusicPlayerTheme

@Composable
fun ProfileScreen(
    onEditButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val preferencesManager = remember{ PreferencesManager(context) }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(id = R.string.profile),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp, top = 40.dp)
        )
        Text(
            text = (
                stringResource(id = R.string.username)
                        + stringResource(id = R.string.profile_fields_delimiter)
                        + preferencesManager.getData("username", "")
                ),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Start)
        )
        Text(
            text = (
                    stringResource(id = R.string.email)
                            + stringResource(id = R.string.profile_fields_delimiter)
                            + preferencesManager.getData("email", "")
                    ),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Start)
        )
        Text(
            text = (
                    stringResource(id = R.string.phone)
                            + stringResource(id = R.string.profile_fields_delimiter)
                            + preferencesManager.getData("phone", "")
                    ),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Start)
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            onClick = onEditButtonClicked
        ) {
            Text(text = stringResource(id = R.string.edit))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileLayoutPreview() {
    MusicPlayerTheme {
        ProfileScreen(
            {}
        )
    }
}