package com.christhperalta.donext.core.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.christhperalta.donext.features.home.presentation.main.BrandGreen


@Composable
fun CustomFloatingActionButton(
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = { /* TODO */ },
        containerColor = BrandGreen,
        contentColor = Color.White
    ) {
        Icon(Icons.Default.Add, contentDescription = "Add Task")
    }
}


