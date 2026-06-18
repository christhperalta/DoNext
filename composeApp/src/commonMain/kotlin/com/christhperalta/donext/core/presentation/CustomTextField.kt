
package com.christhperalta.donext.core.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: StringResource,
    singleLine: Boolean = true,
//    supportingText : String? = "",
//    isError : Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = { onValueChange(it) },
        singleLine = singleLine,
        maxLines = 2,
        label = { Text(text = stringResource(label)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),

//        supportingText = {Text(supportingText ?: "")},
//        isError = isError
    )
}

