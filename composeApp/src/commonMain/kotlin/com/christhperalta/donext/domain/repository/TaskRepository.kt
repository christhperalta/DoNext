package com.christhperalta.donext.domain.repository

import com.christhperalta.donext.core.data.TaskEntity
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAllTasks(): Flow<List<TaskEntity>>
    suspend fun getTaskById(id: Long): TaskEntity?
    suspend fun insertTask(
        description: String,
        category: String,
        priority: String,
        dueDate: String?,
        createdAt: String
    )
    suspend fun updateCompleted(id: Long, isCompleted: Boolean)
    suspend fun deleteTask(id: Long)
    suspend fun countTasksByCategory(categoryName: String): Long
}
