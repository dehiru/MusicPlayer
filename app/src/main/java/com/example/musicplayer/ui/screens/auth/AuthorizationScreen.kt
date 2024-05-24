package com.example.musicplayer.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.musicplayer.R
import com.example.musicplayer.data.PreferencesManager
import com.example.musicplayer.ui.common.EditPasswordField
import com.example.musicplayer.ui.common.EditTextField
import com.example.musicplayer.ui.theme.MusicPlayerTheme

@Composable
fun AuthorizationScreen(
    onAuthorizationPassed: () -> Unit,
    onRegisterButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    var usernameInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var supportingText: Int? by remember { mutableStateOf(null) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .imePadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.authorization),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp, top = 40.dp)
        )
        EditTextField(
            label = R.string.username,
            supportingText = supportingText,
            isError = isError,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            value = usernameInput,
            onValueChanged = { usernameInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
        )
        EditPasswordField(
            label = R.string.password,
            supportingText = supportingText,
            isError = isError,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            value = passwordInput,
            onValueChanged = { passwordInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            onClick = {
                val authPassed = checkAuthorization(
                    preferencesManager,
                    usernameInput,
                    passwordInput
                )
                if (authPassed) {
                    onAuthorizationPassed()
                } else {
                    isError = true
                    supportingText = R.string.wrong_username_or_password
                }
            }
        ) {
            Text(text = stringResource(id = R.string.log_in))
        }
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onRegisterButtonClicked
        ) {
            Text(text = stringResource(id = R.string.register))
        }
    }
}

fun checkAuthorization(
    preferencesManager: PreferencesManager,
    usernameInput: String,
    passwordInput: String
) : Boolean {
    val usernameStored = preferencesManager.getData("username", "")
    val passwordStored = preferencesManager.getData("password", "")
    if ((usernameInput == usernameStored) && (passwordInput == passwordStored)) {
        return true
    } else return false
}

@Preview(showBackground = true)
@Composable
fun AuthorizationLayoutPreview() {
    MusicPlayerTheme {
        AuthorizationScreen(
            {}, {}
        )
    }
}