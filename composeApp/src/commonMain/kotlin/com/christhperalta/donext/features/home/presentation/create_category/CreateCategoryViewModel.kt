package com.christhperalta.donext.features.home.presentation.create_category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.christhperalta.donext.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateCategoryViewModel(
    private val repository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateCategoryState())
    val state: StateFlow<CreateCategoryState> = _uiState

    fun onEvent(event: CreateCategoryEvents) {
        when (event) {
            is CreateCategoryEvents.OnNameChange -> {
                _uiState.update { it.copy(name = event.name) }
            }
            is CreateCategoryEvents.OnIconChange -> {
                _uiState.update { it.copy(selectedIcon = event.icon) }
            }
            is CreateCategoryEvents.OnColorChange -> {
                _uiState.update { it.copy(selectedColor = event.color) }
            }
            CreateCategoryEvents.OnCreateCategory -> {
                val s = _uiState.value
                if (s.name.isBlank()) return
                viewModelScope.launch {
                    repository.insertCategory(
                        name = s.name,
                        iconName = s.selectedIcon,
                        colorHex = s.selectedColor,
                        createdAt = kotlinx.datetime.Clock.System.now().toString()
                    )
                    _uiState.update { CreateCategoryState() }
                }
            }
        }
    }
}
