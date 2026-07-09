package com.christhperalta.donext.features.home.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.christhperalta.donext.core.presentation.CustomFloatingActionButton
import com.christhperalta.donext.core.presentation.CustomText
import org.koin.compose.viewmodel.koinViewModel

data class FilterOption(
    val id: Int,
    val text: String,
    val type: FilterType,
)

@Composable
fun HomeScreen(
    onNavigateToNewTask : ()->Unit,
    onNavigateToEditTask : (Long) -> Unit,
) {
    val vm = koinViewModel<HomeViewModel>()
    val state by vm.state.collectAsStateWithLifecycle()

    val filterOptions = listOf(
        FilterOption(1, "All Tasks", FilterType.ALL_TASKS),
        FilterOption(2, "Priority", FilterType.PRIORITY),
        FilterOption(3, "Focus", FilterType.FOCUS),
        FilterOption(4, "Personal", FilterType.PERSONAL),
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            HomeTopBar(userName = "Home")
        },
        floatingActionButton = {
            CustomFloatingActionButton{
                onNavigateToNewTask()
            }
        },

    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item { HeaderSection(userName = state.userName, taskCount = state.todayTasks.size) }

            item { FilterRow(options = filterOptions, activeFilter = state.activeFilter, onFilterClick = vm::onFilterChanged) }

            if (state.todayTasks.isEmpty()) {
                item {
                    Text(
                        text = "No tasks for today.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 16.dp),
                    )
                }
            }

            items(state.todayTasks) { task ->
                TaskCard(
                    title = task.title,
                    subtitle = task.description,
                    isCompleted = task.isCompleted == 1L,
                    onClick = { onNavigateToEditTask(task.id) },
                    onToggleCompleted = { vm.toggleCompleted(task.id) },
                )
            }
        }
    }
}

@Composable
private fun HeaderSection(userName: String, taskCount: Int) {
    val displayName = userName.ifBlank { "there" }
    Column(modifier = Modifier.padding(top = 30.dp)) {
        Text(
            text = "Hello, $displayName.",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "You have $taskCount task${if (taskCount != 1) "s" else ""} today.",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun FilterRow(
    options: List<FilterOption>,
    activeFilter: FilterType,
    onFilterClick: (FilterType) -> Unit,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(options, key = { it.id }) { option ->
            val isActive = option.type == activeFilter
            val containerColor = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
            val contentColor = if (isActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

            Button(
                onClick = { onFilterClick(option.type) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = containerColor,
                    contentColor = contentColor
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Text(text = option.text)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(userName: String) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        title = {
            CustomText(
                text = userName.ifBlank { "Home" },
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
        },
    )
}

@Composable
private fun TaskCard(
    title: String,
    subtitle: String,
    isCompleted: Boolean = false,
    onClick: () -> Unit = {},
    onToggleCompleted: () -> Unit = {},
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (isCompleted) MaterialTheme.colorScheme.primary else Color.Transparent)
                    .border(
                        width = if (isCompleted) 0.dp else 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = CircleShape,
                    )
                    .clickable(onClick = onToggleCompleted),
                contentAlignment = Alignment.Center,
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = if (isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            IconButton(onClick = onClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowRight, contentDescription = "Open Task")
            }
        }
    }
}
