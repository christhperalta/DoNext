package com.christhperalta.donext.features.home.presentation.create_todo

import com.christhperalta.donext.core.data.CategoryEntity


sealed class NewTaskEvents {
    data class OnDescriptionChange(val description: String) : NewTaskEvents()
    data class OnCategorySelected(val category: CategoryEntity) : NewTaskEvents()
    data object OnCreateTask : NewTaskEvents()
}
