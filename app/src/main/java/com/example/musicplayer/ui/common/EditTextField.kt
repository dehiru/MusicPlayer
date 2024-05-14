package com.example.musicplayer.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun EditTextField(
    @StringRes label: Int,
    isError: Boolean,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    @StringRes supportingText: Int? = null,
) {
        OutlinedTextField(
        value = value,
        isError = isError,
        singleLine = true,
        modifier = modifier,
        onValueChange = onValueChanged,
        label = { Text(stringResource(label)) },
        supportingText = {
            if (supportingText != null) {
                Text(stringResource(supportingText))
            }
        },
        keyboardOptions = keyboardOptions
    )
}