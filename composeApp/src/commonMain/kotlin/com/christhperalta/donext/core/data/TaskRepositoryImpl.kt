package com.christhperalta.donext.core.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.christhperalta.donext.domain.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TaskRepositoryImpl(private val db: DoNextDatabase) : TaskRepository {

    private val queries get() = db.taskQueries

    override fun getAllTasks(): Flow<List<TaskEntity>> {
        return queries.selectAll().asFlow().mapToList(Dispatchers.Default)
    }

    override suspend fun getTaskById(id: Long): TaskEntity? = withContext(Dispatchers.Default) {
        queries.selectById(id).executeAsOneOrNull()
    }

    override suspend fun insertTask(
        description: String,
        category: String,
        priority: String,
        dueDate: String?,
        createdAt: String
    ) = withContext(Dispatchers.Default) {
        queries.insert(
            description = description,
            category = category,
            priority = priority,
            dueDate = dueDate,
            isCompleted = 0L,
            createdAt = createdAt
        )
    }

    override suspend fun updateCompleted(id: Long, isCompleted: Boolean) = withContext(Dispatchers.Default) {
        queries.updateCompleted(isCompleted = if (isCompleted) 1L else 0L, id = id)
    }

    override suspend fun deleteTask(id: Long) = withContext(Dispatchers.Default) {
        queries.deleteById(id)
    }

    override suspend fun countTasksByCategory(categoryName: String): Long = withContext(Dispatchers.Default) {
        queries.countTasksByCategory(categoryName).executeAsOne()
    }
}
