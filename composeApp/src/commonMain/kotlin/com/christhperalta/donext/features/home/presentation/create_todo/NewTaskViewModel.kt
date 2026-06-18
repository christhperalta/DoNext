package com.christhperalta.donext.features.home.presentation.create_todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.christhperalta.donext.domain.repository.CategoryRepository
import com.christhperalta.donext.domain.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewTaskViewModel(
    private val taskRepository: TaskRepository,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewTaskState())
    val state: StateFlow<NewTaskState> = _uiState

    init {
        viewModelScope.launch {
            categoryRepository.getAllCategories().collect { categories ->
                _uiState.update { it.copy(categories = categories) }
            }
        }
    }

    fun onEvent(event: NewTaskEvents) {
        when (event) {
            is NewTaskEvents.OnDescriptionChange -> {
                _uiState.update { it.copy(taskDescription = event.description) }
            }
            is NewTaskEvents.OnCategorySelected -> {
                _uiState.update { it.copy(selectedCategory = event.category) }
            }
            NewTaskEvents.OnCreateTask -> {
                val s = _uiState.value
                if (s.taskDescription.isBlank()) return
                viewModelScope.launch {
                    taskRepository.insertTask(
                        description = s.taskDescription,
                        category = s.selectedCategory?.name ?: "OTHER",
                        priority = "MEDIUM",
                        dueDate = null,
                        createdAt = kotlinx.datetime.Clock.System.now().toString()
                    )
                    _uiState.update { it.copy(taskDescription = "") }
                }
            }
        }
    }
}
