package com.christhperalta.donext.features.home.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.christhperalta.donext.core.data.Settings
import com.christhperalta.donext.core.data.TaskEntity
import com.christhperalta.donext.domain.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

enum class FilterType { ALL_TASKS, PRIORITY, FOCUS, PERSONAL }

data class HomeState(
    val todayTasks: List<TaskEntity> = emptyList(),
    val activeFilter: FilterType = FilterType.ALL_TASKS,
    val userName: String = "",
)

class HomeViewModel(
    private val taskRepository: TaskRepository,
    private val settings: Settings,
) : ViewModel() {

    private val userName = settings.getString("user_name")

    private val _filter = MutableStateFlow(FilterType.ALL_TASKS)

    val state: StateFlow<HomeState> = combine(
        taskRepository.getAllTasks(),
        _filter,
    ) { tasks, filter ->
        val todayStr = kotlinx.datetime.Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
            .toString()
        val todayTasks = tasks.filter { task ->
            task.dueDate == todayStr && task.isCompleted == 0L &&
                when (filter) {
                    FilterType.ALL_TASKS -> true
                    FilterType.PRIORITY -> task.priority == "HIGH"
                    FilterType.FOCUS -> task.description.isBlank()
                    FilterType.PERSONAL -> task.category == "PERSONAL"
                }
        }
        HomeState(todayTasks = todayTasks, activeFilter = filter, userName = userName)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeState())

    fun onFilterChanged(filter: FilterType) {
        _filter.update { filter }
    }

    fun toggleCompleted(taskId: Long) {
        viewModelScope.launch {
            val task = taskRepository.getTaskById(taskId) ?: return@launch
            taskRepository.updateCompleted(taskId, task.isCompleted == 0L)
        }
    }
}
