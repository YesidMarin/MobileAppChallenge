package com.yesidmarin.mobileappchallenge.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SearchField(
    query: String,
    queryError: Boolean,
    focusRequester: FocusRequester,
    onQueryChange: (String) -> Unit,
    onFocusClear: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        label = { Text("Buscar en Mercado Libre") },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(remember { focusRequester }),
        singleLine = true,
        isError = queryError,
        supportingText = {
            if (queryError) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Completa este campo para buscar",
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        trailingIcon = {
            if (queryError)
                Icon(Icons.Filled.Info, "error", tint = MaterialTheme.colorScheme.error)
        },
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                keyboardController?.hide()
                onFocusClear()
            }
        )
    )
}

@Preview
@Composable
fun SearchFieldPreview() {
    SearchField(
        query = "Buscar producto",
        queryError = false,
        onQueryChange = {},
        focusRequester = FocusRequester(),
        onFocusClear = {}
    )
}