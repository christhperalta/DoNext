package com.christhperalta.donext.features.home.presentation.create_todo

import com.christhperalta.donext.core.data.CategoryEntity
import com.christhperalta.donext.core.model.TaskPriority


sealed class NewTaskEvents {
    data class OnTitleChange(val title: String) : NewTaskEvents()
    data class OnDescriptionChange(val description: String) : NewTaskEvents()
    data class OnCategorySelected(val category: CategoryEntity) : NewTaskEvents()
    data class OnPrioritySelected(val priority: TaskPriority) : NewTaskEvents()
    data class OnDueDateSelected(val date: String?) : NewTaskEvents()
    data object OnCreateTask : NewTaskEvents()
    data object OnUpdateTask : NewTaskEvents()
    data object OnDeleteTask : NewTaskEvents()
}
