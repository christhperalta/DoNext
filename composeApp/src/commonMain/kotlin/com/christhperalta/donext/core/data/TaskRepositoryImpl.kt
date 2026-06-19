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

    override fun getTasksByCategory(categoryName: String): Flow<List<TaskEntity>> {
        return queries.selectByCategory(categoryName).asFlow().mapToList(Dispatchers.Default)
    }

    override fun getRestorableTasksByCategory(categoryName: String): Flow<List<TaskEntity>> {
        val threshold = kotlinx.datetime.Instant.fromEpochMilliseconds(
            kotlinx.datetime.Clock.System.now().toEpochMilliseconds() - 172_800_000L
        ).toString()
        return queries.selectRestorableByCategory(categoryName, threshold).asFlow().mapToList(Dispatchers.Default)
    }

    override suspend fun getTaskById(id: Long): TaskEntity? = withContext(Dispatchers.Default) {
        queries.selectById(id).executeAsOneOrNull()
    }

    override suspend fun insertTask(
        title: String,
        description: String,
        category: String,
        priority: String,
        dueDate: String?,
        createdAt: String
    ) = withContext(Dispatchers.Default) {
        queries.insert(
            title = title,
            description = description,
            category = category,
            priority = priority,
            dueDate = dueDate,
            isCompleted = 0L,
            createdAt = createdAt
        )
    }

    override suspend fun updateTask(
        id: Long,
        title: String,
        description: String,
        category: String,
        priority: String,
        dueDate: String?,
    ) = withContext(Dispatchers.Default) {
        queries.updateTask(
            id = id,
            title = title,
            description = description,
            category = category,
            priority = priority,
            dueDate = dueDate,
        )
    }

    override suspend fun updateCompleted(id: Long, isCompleted: Boolean) = withContext(Dispatchers.Default) {
        queries.updateCompleted(isCompleted = if (isCompleted) 1L else 0L, id = id)
    }

    override suspend fun deleteTask(id: Long) = withContext(Dispatchers.Default) {
        queries.deleteById(id)
    }

    override suspend fun softDeleteTask(id: Long) = withContext(Dispatchers.Default) {
        val deletedAt = kotlinx.datetime.Clock.System.now().toString()
        queries.softDeleteById(deletedAt = deletedAt, id = id)
    }

    override suspend fun restoreTask(id: Long) = withContext(Dispatchers.Default) {
        queries.restoreById(id)
    }

    override suspend fun permanentDeleteOldTasks() = withContext(Dispatchers.Default) {
        val threshold = kotlinx.datetime.Instant.fromEpochMilliseconds(
            kotlinx.datetime.Clock.System.now().toEpochMilliseconds() - 172_800_000L
        ).toString()
        queries.permanentDeleteOld(threshold)
    }

    override suspend fun countTasksByCategory(categoryName: String): Long = withContext(Dispatchers.Default) {
        queries.countTasksByCategory(categoryName).executeAsOne()
    }
}
