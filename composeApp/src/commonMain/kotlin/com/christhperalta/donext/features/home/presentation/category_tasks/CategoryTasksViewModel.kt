package com.christhperalta.donext.features.home.presentation.category_tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.christhperalta.donext.core.data.TaskEntity
import com.christhperalta.donext.domain.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class CategoryTasksState(
    val categoryName: String = "",
    val activeTasks: List<TaskEntity> = emptyList(),
    val restorableTasks: List<TaskEntity> = emptyList(),
)

class CategoryTasksViewModel(
    private val taskRepository: TaskRepository,
) : ViewModel() {

    private val _categoryName = MutableStateFlow("")

    val state: StateFlow<CategoryTasksState> = _categoryName
        .flatMapLatest { name ->
            if (name.isBlank()) {
                flowOf(CategoryTasksState())
            } else {
                combine(
                    taskRepository.getTasksByCategory(name),
                    taskRepository.getRestorableTasksByCategory(name),
                ) { active, restorable ->
                    CategoryTasksState(
                        categoryName = name,
                        activeTasks = active,
                        restorableTasks = restorable,
                    )
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CategoryTasksState())

    fun loadCategory(name: String) {
        _categoryName.value = name
    }

    fun toggleCompleted(taskId: Long) {
        viewModelScope.launch {
            val task = taskRepository.getTaskById(taskId) ?: return@launch
            taskRepository.updateCompleted(taskId, task.isCompleted == 0L)
        }
    }

    fun softDeleteTask(taskId: Long) {
        viewModelScope.launch {
            taskRepository.softDeleteTask(taskId)
        }
    }

    fun restoreTask(taskId: Long) {
        viewModelScope.launch {
            taskRepository.restoreTask(taskId)
        }
    }
}