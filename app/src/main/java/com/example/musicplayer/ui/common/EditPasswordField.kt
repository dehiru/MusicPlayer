package com.example.musicplayer.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.musicplayer.R

@Composable
fun EditPasswordField(
    @StringRes label: Int,
    isError: Boolean,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    @StringRes supportingText: Int? = null,
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    OutlinedTextField(
        trailingIcon = {
            val icon = if (isPasswordVisible) {
                ImageVector.vectorResource(R.drawable.visibility_off)
            } else {
                ImageVector.vectorResource(R.drawable.visibility)
            }
            IconButton(onClick = {isPasswordVisible = !isPasswordVisible}) {
                Icon(imageVector = icon, contentDescription = null)
            }
        },
        visualTransformation = if (isPasswordVisible) {
            VisualTransformation.None
        } else PasswordVisualTransformation(),
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