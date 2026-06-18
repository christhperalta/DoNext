package com.christhperalta.donext.features.home.presentation.create_category

sealed class CreateCategoryEvents {
    data class OnNameChange(val name: String) : CreateCategoryEvents()
    data class OnIconChange(val icon: String) : CreateCategoryEvents()
    data class OnColorChange(val color: String) : CreateCategoryEvents()
    data object OnCreateCategory : CreateCategoryEvents()
}
