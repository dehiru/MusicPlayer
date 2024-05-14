package com.example.musicplayer.ui.screens.profile

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
import com.example.musicplayer.ui.common.EditTextField
import com.example.musicplayer.ui.theme.MusicPlayerTheme

@Composable
fun EditProfileScreen(
    onSaveButtonClicked: () -> Unit,
    onCanselButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val preferencesManager = remember{ PreferencesManager(context) }
    var usernameInput by remember {
        mutableStateOf(
            preferencesManager.getData("username", "")
        )
    }
    var emailInput by remember {
        mutableStateOf(
            preferencesManager.getData("email", "")
        )
    }
    var phoneInput by remember {
        mutableStateOf(
            preferencesManager.getData("phone", "")
        )
    }
    var isError by remember { mutableStateOf(false) }
    var isUsernameTooShort  by remember { mutableStateOf(false) }
    var isUsernameTooLong  by remember { mutableStateOf(false) }
    var isEmailWrong  by remember { mutableStateOf(false) }
    var isPhoneTooShort  by remember { mutableStateOf(false) }
    var isPhoneTooLong  by remember { mutableStateOf(false) }
    var usernameSupportingText: Int? by remember { mutableStateOf(null) }
    var emailSupportingText: Int? by remember { mutableStateOf(null) }
    var phoneSupportingText: Int? by remember { mutableStateOf(null) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(id = R.string.profile_editing),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp, top = 40.dp)
        )
        EditTextField(
            label = R.string.username,
            supportingText = usernameSupportingText,
            isError = isUsernameTooShort || isUsernameTooLong,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            value = usernameInput,
            onValueChanged = { usernameInput = it },
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(),
        )
        EditTextField(
            label = R.string.email,
            supportingText = emailSupportingText,
            isError = isEmailWrong,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            value = emailInput,
            onValueChanged = { emailInput = it },
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(),
        )
        EditTextField(
            label = R.string.phone,
            supportingText = phoneSupportingText,
            isError = isPhoneTooShort || isPhoneTooLong,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            ),
            value = phoneInput,
            onValueChanged = { phoneInput = it },
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(),
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            onClick = {
                if (usernameInput.length < 3) {
                    isUsernameTooShort = true
                    usernameSupportingText = R.string.username_too_short
                } else if (usernameInput.length > 30)  {
                    isUsernameTooLong = true
                    usernameSupportingText = R.string.username_too_long
                } else {
                    isUsernameTooShort = false
                    isUsernameTooLong = false
                    usernameSupportingText = null
                }
                if (
                    emailInput.length < 3 ||
                    !emailInput.contains("@") ||
                    emailInput.length > 100
                    )  {
                    isEmailWrong = true
                    emailSupportingText = R.string.email_wrong
                } else {
                    isEmailWrong = false
                    phoneSupportingText = null
                }
                if (phoneInput.length < 9) {
                    isPhoneTooShort = true
                    phoneSupportingText = R.string.phone_too_short
                } else if (phoneInput.length > 12)  {
                    isPhoneTooLong = true
                    phoneSupportingText = R.string.phone_too_long
                } else {
                    isPhoneTooShort = false
                    isPhoneTooLong = false
                    phoneSupportingText = null
                }
                isError = (isUsernameTooShort ||
                        isUsernameTooLong ||
                        isEmailWrong ||
                        isPhoneTooShort ||
                        isPhoneTooLong
                        )
                if (!isError) {
                    preferencesManager.saveData("username", usernameInput)
                    preferencesManager.saveData("email", emailInput)
                    preferencesManager.saveData("phone", phoneInput)
                    onSaveButtonClicked()
                }
            }
        ) {
            Text(text = stringResource(id = R.string.save))
        }
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onCanselButtonClicked
        ) {
            Text(text = stringResource(id = R.string.cansel))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileLayoutPreview() {
    MusicPlayerTheme {
        EditProfileScreen(
            {}, {}
        )
    }
}