package com.christhperalta.donext.core.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.christhperalta.donext.domain.repository.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class CategoryRepositoryImpl(private val db: DoNextDatabase) : CategoryRepository {

    private val queries get() = db.categoryQueries

    override fun getAllCategories(): Flow<List<CategoryEntity>> {
        return queries.selectAll().asFlow().mapToList(Dispatchers.Default)
    }

    override suspend fun insertCategory(
        name: String,
        iconName: String,
        colorHex: String,
        createdAt: String
    ) = withContext(Dispatchers.Default) {
        queries.insert(
            name = name,
            iconName = iconName,
            colorHex = colorHex,
            createdAt = createdAt
        )
    }

    override suspend fun deleteCategory(id: Long) = withContext(Dispatchers.Default) {
        queries.deleteById(id)
    }

    override suspend fun seedDefaultCategories() = withContext(Dispatchers.Default) {
        val now = kotlinx.datetime.Clock.System.now().toString()
        listOf(
            Triple("PERSONAL", "Person", "#6C63FF"),
            Triple("WORK", "Work", "#60DF20"),
            Triple("SHOPPING", "ShoppingCart", "#FFD93D"),
            Triple("HEALTH", "Favorite", "#FF6B6B"),
            Triple("STUDY", "School", "#4ECDC4"),
        ).forEach { (name, icon, color) ->
            queries.insertOrIgnore(
                name = name,
                iconName = icon,
                colorHex = color,
                createdAt = now
            )
        }
    }
}
