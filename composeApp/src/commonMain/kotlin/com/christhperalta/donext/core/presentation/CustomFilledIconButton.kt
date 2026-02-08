package com.christhperalta.donext.core.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CustomFilledIconButton(
    modifier: Modifier = Modifier,
    icon : ImageVector,
    contentDescription : String,
    color : Color ,
    onClick: () -> Unit
) {
    FilledIconButton(
        modifier = modifier,
        colors = IconButtonDefaults.filledIconButtonColors(
          containerColor = color
        ),
        onClick = {onClick()}
    ) {
        Icon(icon, contentDescription = contentDescription)
    }
}


