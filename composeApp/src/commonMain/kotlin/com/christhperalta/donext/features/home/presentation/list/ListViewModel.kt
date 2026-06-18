package com.christhperalta.donext.features.home.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.christhperalta.donext.core.data.CategoryEntity
import com.christhperalta.donext.domain.repository.CategoryRepository
import com.christhperalta.donext.domain.repository.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class CategoryWithCount(
    val category: CategoryEntity,
    val taskCount: Long,
)

data class ListState(
    val categories: List<CategoryWithCount> = emptyList(),
)

class ListViewModel(
    private val categoryRepository: CategoryRepository,
    private val taskRepository: TaskRepository,
) : ViewModel() {

    val state: StateFlow<ListState> = combine(
        categoryRepository.getAllCategories(),
        taskRepository.getAllTasks()
    ) { categories, tasks ->
        ListState(
            categories = categories.map { cat ->
                CategoryWithCount(
                    category = cat,
                    taskCount = tasks.count { it.category == cat.name }.toLong()
                )
            }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ListState())
}
