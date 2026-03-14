package com.christhperalta.donext.features.home.presentation.create_todo

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class NewTaskViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NewTackState())
    val state : StateFlow<NewTackState> = _uiState




    fun onEvent(event: NewTaskEvents) {
       when(event) {
           is NewTaskEvents.OnDescriptionChange -> {
               _uiState.update { state ->
                   state.copy(taskDescription = event.description)
               }
           }

           NewTaskEvents.OnCreateTask -> {
               println("Christh Create task ${state.value.taskDescription}")
               _uiState.update { state ->
                   state.copy(taskDescription = "")
               }
           }
       }
    }


}