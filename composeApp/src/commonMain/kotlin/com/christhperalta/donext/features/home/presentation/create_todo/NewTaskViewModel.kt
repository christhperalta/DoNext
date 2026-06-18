package com.christhperalta.donext.features.home.presentation.create_todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.christhperalta.donext.domain.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewTaskViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewTaskState())
    val state : StateFlow<NewTaskState> = _uiState

    fun onEvent(event: NewTaskEvents) {
       when(event) {
           is NewTaskEvents.OnDescriptionChange -> {
               _uiState.update { state ->
                   state.copy(taskDescription = event.description)
               }
           }

           NewTaskEvents.OnCreateTask -> {
               val description = _uiState.value.taskDescription
               if (description.isBlank()) return
               viewModelScope.launch {
                   repository.insertTask(
                       description = description,
                       category = "PERSONAL",
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
