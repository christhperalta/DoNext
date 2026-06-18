package com.christhperalta.donext.features.home.presentation.create_todo

import com.christhperalta.donext.core.data.CategoryEntity
import com.christhperalta.donext.core.model.TaskPriority


data class NewTaskState (
    val taskDescription: String = "",
    val categories: List<CategoryEntity> = emptyList(),
    val selectedCategory: CategoryEntity? = null,
//    val taskPriority: TaskPriority = TaskPriority.LOW,
)
