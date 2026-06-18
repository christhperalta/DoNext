package com.christhperalta.donext.features.home.presentation.create_todo
import com.christhperalta.donext.core.model.TaskCategory
import com.christhperalta.donext.core.model.TaskPriority


data class NewTaskState (
    val taskDescription: String = "",
//    val taskCategory: TaskCategory = TaskCategory.PERSONAL,
//    val taskPriority: TaskPriority = TaskPriority.LOW,
)
