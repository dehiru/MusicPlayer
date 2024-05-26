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
fun RegistrationScreen(
    onRegisterButtonClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    var usernameInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var usernameSupportingText: Int? by remember { mutableStateOf(null) }
    var passwordSupportingText: Int? by remember { mutableStateOf(null) }
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
            text = stringResource(id = R.string.registration),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp, top = 40.dp)
        )
        EditTextField(
            label = R.string.username,
            supportingText = usernameSupportingText,
            isError = usernameError,
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
            supportingText = passwordSupportingText,
            isError = passwordError,
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
                if (usernameInput.length < 3) {
                    usernameError = true
                    usernameSupportingText = R.string.username_too_short
                } else if (usernameInput.length > 30)  {
                    usernameError = true
                    usernameSupportingText = R.string.username_too_long
                } else {
                    usernameError = false
                    usernameSupportingText = null
                }
                if (passwordInput.length < 4) {
                    passwordError = true
                    passwordSupportingText = R.string.password_too_short
                } else if (passwordInput.length > 30) {
                    passwordError = true
                    passwordSupportingText = R.string.password_too_long
                } else {
                    passwordError = false
                    passwordSupportingText = null
                }
                if (!(usernameError || passwordError)) {
                    preferencesManager.saveData("username", usernameInput)
                    preferencesManager.saveData("password", passwordInput)
                    onRegisterButtonClicked()
                }
            }
        ) {
            Text(text = stringResource(id = R.string.register))
        }
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onCancelButtonClicked
        ) {
            Text(text = stringResource(id = R.string.cancel))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationLayoutPreview() {
    MusicPlayerTheme {
        RegistrationScreen(
            {}, {}
        )
    }
}