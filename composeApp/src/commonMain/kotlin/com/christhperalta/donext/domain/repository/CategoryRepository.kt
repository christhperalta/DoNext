package com.christhperalta.donext.domain.repository

import com.christhperalta.donext.core.data.CategoryEntity
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategories(): Flow<List<CategoryEntity>>
    suspend fun insertCategory(name: String, iconName: String, colorHex: String, createdAt: String)
    suspend fun deleteCategory(id: Long)
    suspend fun seedDefaultCategories()
}
