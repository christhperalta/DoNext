package com.christhperalta.donext.features.home.presentation.create_todo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.christhperalta.donext.core.data.CategoryEntity
import com.christhperalta.donext.core.model.TaskPriority
import com.christhperalta.donext.core.presentation.CategoryDefaults
import com.christhperalta.donext.core.presentation.CustomFilledIconButton
import com.christhperalta.donext.core.presentation.CustomText
import com.christhperalta.donext.core.presentation.theme.PriorityHigh
import com.christhperalta.donext.core.presentation.theme.PriorityLow
import com.christhperalta.donext.core.presentation.theme.PriorityMedium
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NewTaskScreen(
    taskId: Long? = null,
    onBack: () -> Unit,
    vm: NewTaskViewModel = koinViewModel()
) {
    val uiState by vm.state.collectAsStateWithLifecycle()
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(taskId) {
        if (taskId != null) {
            vm.loadTask(taskId)
        } else {
            vm.resetState()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            NewTaskTopBar(
                onBack = onBack,
                isEditMode = uiState.isEditMode,
                onDelete = { showDeleteDialog = true },
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(start = 16.dp, end = 16.dp, bottom = 30.dp)
        ) {

           Column (
               modifier = Modifier.align(Alignment.TopStart).padding(top = 32.dp),
               verticalArrangement = Arrangement.spacedBy(16.dp)
           ){
               CustomInputScreen(
                     placeholder = "Title",
                     value = uiState.taskTitle,
                     onValueChange = { vm.onEvent(NewTaskEvents.OnTitleChange(it)) },
                     fontSize = 22.sp,
                     fontWeight = FontWeight.Bold
                 )


                CustomInputScreen(
                     placeholder = "Note",
                     value = uiState.taskDescription,
                     onValueChange = { vm.onEvent(NewTaskEvents.OnDescriptionChange(it)) },
                     fontSize = 16.sp,
                     fontWeight = FontWeight.Normal
                 )
           }

            Column(
                modifier = Modifier.align(Alignment.Center),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {

                CategorySelector(
                    label = "Category",
                    selectedCategory = uiState.selectedCategory,
                    categories = uiState.categories,
                    onCategorySelected = { vm.onEvent(NewTaskEvents.OnCategorySelected(it)) },
                )

                DueDateSelector(
                    label = "Due Date",
                    selectedDate = uiState.dueDate,
                    onDateSelected = { vm.onEvent(NewTaskEvents.OnDueDateSelected(it)) },
                )

                PrioritySelector(
                    label = "Priority",
                    selectedPriority = uiState.taskPriority,
                    onPrioritySelected = { vm.onEvent(NewTaskEvents.OnPrioritySelected(it)) },
                )
            }

            CustomFilledIconButton(
                modifier = Modifier.size(80.dp).align(Alignment.BottomCenter),
                color = MaterialTheme.colorScheme.primary,
                onClick = {
                    if (uiState.isEditMode) {
                        vm.onEvent(NewTaskEvents.OnUpdateTask)
                    } else {
                        vm.onEvent(NewTaskEvents.OnCreateTask)
                    }
                }
            ) {
                Icon(Icons.Default.Check, contentDescription = "Check", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Task") },
            text = { Text("Are you sure you want to delete this task?") },
            confirmButton = {
                TextButton(onClick = {
                    vm.deleteTask()
                    showDeleteDialog = false
                    onBack()
                }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskTopBar(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    isEditMode: Boolean = false,
    onDelete: () -> Unit = {},
) {
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        title = {
            CustomText(
                text = if (isEditMode) "Edit Task" else "New Task",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            if (isEditMode) {
                CustomFilledIconButton(
                    color = MaterialTheme.colorScheme.errorContainer,
                    onClick = onDelete,
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
            CustomFilledIconButton(
                color = MaterialTheme.colorScheme.surfaceVariant,
                onClick = { onBack() }
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelector(
    label: String,
    selectedCategory: CategoryEntity?,
    categories: List<CategoryEntity>?,
    onCategorySelected: ((CategoryEntity) -> Unit)?,
) {
    val iconGreen = MaterialTheme.colorScheme.primary
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant
    var expanded by remember { mutableStateOf(false) }

    val hasDropdown = categories != null && onCategorySelected != null
    val icon = if (selectedCategory != null)
        CategoryDefaults.iconByName(selectedCategory.iconName)
    else
        Icons.AutoMirrored.Filled.List
    val iconTint = if (selectedCategory != null)
        CategoryDefaults.parseColor(selectedCategory.colorHex)
    else
        iconGreen
    val dropDownLabel = selectedCategory?.name ?: "Select"

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
                .clickable { if (hasDropdown) expanded = true },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = iconTint,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = label,
                        color = labelColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = dropDownLabel,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Dropdown",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        if (hasDropdown) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                if (categories.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("No categories yet") },
                        onClick = { expanded = false },
                    )
                } else {
                    categories.forEach { category ->
                        val catColor = CategoryDefaults.parseColor(category.colorHex)
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(catColor),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Icon(
                                            imageVector = CategoryDefaults.iconByName(category.iconName),
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp),
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(text = category.name)
                                }
                            },
                            onClick = {
                                onCategorySelected(category)
                                expanded = false
                            },
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DueDateSelector(
    label: String,
    selectedDate: String?,
    onDateSelected: (String?) -> Unit,
) {
    val iconGreen = MaterialTheme.colorScheme.primary
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant
    var showDialog by remember { mutableStateOf(false) }

    val displayText = if (selectedDate != null) {
        val parts = selectedDate.split("-")
        val monthNames = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        val month = parts[1].toIntOrNull()?.let { monthNames.getOrNull(it - 1) } ?: parts[1]
        "${month} ${parts[2]}, ${parts[0]}"
    } else {
        "Set date"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = label,
                    tint = iconGreen,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = label,
                    color = labelColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = displayText,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (selectedDate != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "✕",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 18.sp,
                        modifier = Modifier.clickable {
                            onDateSelected(null)
                        }
                    )
                }
            }
        }
    }

    if (showDialog) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate?.let {
                kotlinx.datetime.Instant.parse("${it}T00:00:00Z").toEpochMilliseconds()
            }
        )

        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val instant = Instant.fromEpochMilliseconds(millis)
                        val localDate = instant.toLocalDateTime(TimeZone.UTC).date
                        onDateSelected(localDate.toString())
                    }
                    showDialog = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioritySelector(
    label: String,
    selectedPriority: TaskPriority,
    onPrioritySelected: (TaskPriority) -> Unit,
) {
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant
    var expanded by remember { mutableStateOf(false) }

    val (icon, iconTint) = when (selectedPriority) {
        TaskPriority.LOW -> Icons.Default.ArrowDownward to PriorityLow
        TaskPriority.MEDIUM -> Icons.Default.Remove to PriorityMedium
        TaskPriority.HIGH -> Icons.Default.ArrowUpward to PriorityHigh
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
                .clickable { expanded = true },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = iconTint,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = label,
                        color = labelColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = selectedPriority.name,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Dropdown",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            TaskPriority.entries.forEach { priority ->
                val (itemIcon, itemTint) = when (priority) {
                    TaskPriority.LOW -> Icons.Default.ArrowDownward to PriorityLow
                    TaskPriority.MEDIUM -> Icons.Default.Remove to PriorityMedium
                    TaskPriority.HIGH -> Icons.Default.ArrowUpward to PriorityHigh
                }
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = itemIcon,
                                contentDescription = null,
                                tint = itemTint,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = priority.name)
                        }
                    },
                    onClick = {
                        onPrioritySelected(priority)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
fun CustomInputScreen(
    placeholder: String = "",
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit,
    fontSize: TextUnit = 18.sp,
    fontWeight: FontWeight = FontWeight.Normal,
) {

    Box(
        modifier = modifier
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                fontSize = fontSize,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = fontWeight
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = TextStyle(
                                fontSize = fontSize,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = fontWeight
                            )
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}
