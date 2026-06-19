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

    fun loadTask(taskId: Long) {
        viewModelScope.launch {
            val task = taskRepository.getTaskById(taskId) ?: return@launch
            val selectedCategory = _uiState.value.categories.find { it.name == task.category }
            _uiState.update {
                it.copy(
                    taskTitle = task.title,
                    taskDescription = task.description,
                    selectedCategory = selectedCategory,
                    taskPriority = com.christhperalta.donext.core.model.TaskPriority.valueOf(task.priority),
                    dueDate = task.dueDate,
                    isEditMode = true,
                    editingTaskId = taskId,
                )
            }
        }
    }

    fun onEvent(event: NewTaskEvents) {
        when (event) {
            is NewTaskEvents.OnTitleChange -> {
                _uiState.update { it.copy(taskTitle = event.title) }
            }
            is NewTaskEvents.OnDescriptionChange -> {
                _uiState.update { it.copy(taskDescription = event.description) }
            }
            is NewTaskEvents.OnCategorySelected -> {
                _uiState.update { it.copy(selectedCategory = event.category) }
            }
            is NewTaskEvents.OnPrioritySelected -> {
                _uiState.update { it.copy(taskPriority = event.priority) }
            }
            is NewTaskEvents.OnDueDateSelected -> {
                _uiState.update { it.copy(dueDate = event.date) }
            }
            NewTaskEvents.OnCreateTask -> {
                val s = _uiState.value
                if (s.taskTitle.isBlank() || s.taskDescription.isBlank()) return
                viewModelScope.launch {
                    taskRepository.insertTask(
                        title = s.taskTitle,
                        description = s.taskDescription,
                        category = s.selectedCategory?.name ?: "OTHER",
                        priority = s.taskPriority.name,
                        dueDate = s.dueDate,
                        createdAt = kotlinx.datetime.Clock.System.now().toString()
                    )
                    _uiState.update { it.copy(taskTitle = "", taskDescription = "", dueDate = null) }
                }
            }
            NewTaskEvents.OnUpdateTask -> {
                val s = _uiState.value
                val taskId = s.editingTaskId ?: return
                if (s.taskTitle.isBlank() || s.taskDescription.isBlank()) return
                viewModelScope.launch {
                    taskRepository.updateTask(
                        id = taskId,
                        title = s.taskTitle,
                        description = s.taskDescription,
                        category = s.selectedCategory?.name ?: "OTHER",
                        priority = s.taskPriority.name,
                        dueDate = s.dueDate,
                    )
                }
            }
            NewTaskEvents.OnDeleteTask -> {
                val taskId = _uiState.value.editingTaskId ?: return
                _uiState.update { it.copy(editingTaskId = taskId) }
            }
        }
    }

    fun deleteTask() {
        val taskId = _uiState.value.editingTaskId ?: return
        viewModelScope.launch {
            taskRepository.deleteTask(taskId)
        }
    }
}