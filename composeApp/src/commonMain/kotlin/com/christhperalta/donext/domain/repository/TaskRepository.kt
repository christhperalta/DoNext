package com.christhperalta.donext.domain.repository

import com.christhperalta.donext.core.data.TaskEntity
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAllTasks(): Flow<List<TaskEntity>>
    suspend fun getTaskById(id: Long): TaskEntity?
    suspend fun insertTask(
        title: String,
        description: String,
        category: String,
        priority: String,
        dueDate: String?,
        createdAt: String
    )
    suspend fun updateTask(
        id: Long,
        title: String,
        description: String,
        category: String,
        priority: String,
        dueDate: String?,
    )
    suspend fun updateCompleted(id: Long, isCompleted: Boolean)
    suspend fun deleteTask(id: Long)
    suspend fun countTasksByCategory(categoryName: String): Long
    fun getTasksByCategory(categoryName: String): Flow<List<TaskEntity>>
    fun getRestorableTasksByCategory(categoryName: String): Flow<List<TaskEntity>>
    suspend fun softDeleteTask(id: Long)
    suspend fun restoreTask(id: Long)
    suspend fun permanentDeleteOldTasks()
}
