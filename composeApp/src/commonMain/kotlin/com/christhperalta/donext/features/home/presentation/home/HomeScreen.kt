package com.christhperalta.donext.features.home.presentation.home

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.christhperalta.donext.features.home.presentation.main.BrandGreen
import donext.composeapp.generated.resources.Res
import donext.composeapp.generated.resources.profile_img
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

data class FilterOption(
    val id: Int,
    val text: String,
    val type: FilterType,
)

@Composable
fun HomeScreen(
    onNavigateToNewTask : ()->Unit,
    onNavigateToProfile : ()->Unit,
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
            HomeTopBar(userName = state.userName ,onNavigateToProfile = onNavigateToProfile)
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
            color = BrandGreen
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
            val containerColor = if (isActive) BrandGreen else Color.White
            val contentColor = if (isActive) Color.White else Color.Black

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
private fun HomeTopBar(userName: String ,onNavigateToProfile : ()->Unit) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        navigationIcon = {
            Image(
                painter = painterResource(Res.drawable.profile_img),
                contentDescription = "Profile",
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(40.dp)
                    .clip(CircleShape)
            )
        },
        title = { Text(text = userName.ifBlank { "Home" }, fontWeight = FontWeight.Bold) },
        actions = {
            IconButton(onClick = { (onNavigateToProfile()) }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
        }
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
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
                    .background(if (isCompleted) BrandGreen else Color.Transparent)
                    .border(
                        width = if (isCompleted) 0.dp else 1.dp,
                        color = Color.LightGray,
                        shape = CircleShape,
                    )
                    .clickable(onClick = onToggleCompleted),
                contentAlignment = Alignment.Center,
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = Color.White,
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
                    color = if (isCompleted) Color.Gray else Color.Black,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                )
            }

            IconButton(onClick = onClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowRight, contentDescription = "Open Task")
            }
        }
    }
}
