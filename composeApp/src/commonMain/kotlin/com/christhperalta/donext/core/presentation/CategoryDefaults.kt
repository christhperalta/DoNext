package com.christhperalta.donext.core.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

object CategoryDefaults {

    data class IconOption(val key: String, val icon: ImageVector)

    val iconOptions = listOf(
        IconOption("Work", Icons.Default.Work),
        IconOption("Person", Icons.Default.Person),
        IconOption("ShoppingCart", Icons.Default.ShoppingCart),
        IconOption("Favorite", Icons.Default.Favorite),
        IconOption("School", Icons.Default.School),
        IconOption("AttachMoney", Icons.Default.AttachMoney),
        IconOption("Flight", Icons.Default.Flight),
        IconOption("MoreHoriz", Icons.Default.MoreHoriz),
    )

    val colorOptions = listOf(
        "#60DF20",
        "#FF6B6B",
        "#4ECDC4",
        "#FFD93D",
        "#6C63FF",
        "#FF8A80",
        "#A8E6CF",
        "#D4A5A5",
    )

    fun iconByName(key: String): ImageVector =
        iconOptions.find { it.key == key }?.icon ?: Icons.Default.MoreHoriz

    fun parseColor(hex: String): Color {
        val cleanHex = hex.removePrefix("#")
        return Color(("FF$cleanHex").toLong(16))
    }
}
