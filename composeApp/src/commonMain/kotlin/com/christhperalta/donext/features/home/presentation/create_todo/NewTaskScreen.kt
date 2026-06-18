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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.christhperalta.donext.core.data.CategoryEntity
import com.christhperalta.donext.core.presentation.CategoryDefaults
import com.christhperalta.donext.core.presentation.CustomFilledIconButton
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NewTaskScreen(
    onBack: () -> Unit,
    vm: NewTaskViewModel = koinViewModel()
) {

    val uiState by vm.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { NewTaskTopBar(onBack = onBack) }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(start = 16.dp, end = 16.dp, bottom = 30.dp)
        ) {

            CustomInputScreen(
                modifier = Modifier.align(Alignment.TopStart),
                value = uiState.taskDescription,
                onValueChange = { vm.onEvent(NewTaskEvents.OnDescriptionChange(it)) }
            )

            Column(
                modifier = Modifier.align(Alignment.Center),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {

                CategorySelector(
                    label = "Category",
                    dropDownLabel = uiState.selectedCategory?.name ?: "Select",
                    icon = if (uiState.selectedCategory != null)
                        CategoryDefaults.iconByName(uiState.selectedCategory!!.iconName)
                    else
                        Icons.AutoMirrored.Filled.List,
                    iconDescription = "Category icon",
                    iconTint = if (uiState.selectedCategory != null)
                        CategoryDefaults.parseColor(uiState.selectedCategory!!.colorHex)
                    else
                        Color(0xFF81C784),
                    categories = uiState.categories,
                    onCategorySelected = { vm.onEvent(NewTaskEvents.OnCategorySelected(it)) },
                )

                CategorySelector(
                    label = "Due Date",
                    dropDownLabel = "Today, 5:00 PM",
                    icon = Icons.Default.CalendarMonth,
                    iconDescription = "CalendarMonth",
                )

                CategorySelector(
                    label = "Priority",
                    dropDownLabel = "Medium",
                    icon = Icons.Default.PriorityHigh,
                    iconDescription = "PriorityHigh",
                )
            }

            CustomFilledIconButton(
                modifier = Modifier.size(80.dp).align(Alignment.BottomCenter),
                color = Color(0xFF81C784),
                onClick = { vm.onEvent(NewTaskEvents.OnCreateTask) }
            ) {
                Icon(Icons.Default.Check, contentDescription = "Check", tint = Color.White)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskTopBar(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        title = {},
        actions = {
            CustomFilledIconButton(
                color = Color(0xFFF1F5EE),
                onClick = { onBack() }
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }
    )
}


@Composable
fun CategorySelector(
    label: String,
    dropDownLabel: String,
    icon: ImageVector,
    iconDescription: String,
    iconTint: Color = Color(0xFF81C784),
    categories: List<CategoryEntity>? = null,
    onCategorySelected: ((CategoryEntity) -> Unit)? = null,
) {

    val iconGreen = Color(0xFF81C784)
    val labelColor = Color(0xFF546E7A)
    var expanded by remember { mutableStateOf(false) }

    val hasDropdown = categories != null && onCategorySelected != null

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (hasDropdown) Modifier.clickable { expanded = true } else Modifier),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
                    contentDescription = iconDescription,
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
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Dropdown",
                    tint = Color(0xFF90A4AE),
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        if (hasDropdown) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                if (categories!!.isEmpty()) {
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
                                onCategorySelected!!(category)
                                expanded = false
                            },
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun CustomInputScreen(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit
) {

    Box(
        modifier = modifier
            .padding(16.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                fontSize = 22.sp,
                color = Color(0xFF1A1C1A),
                fontWeight = FontWeight.Medium
            ),
            cursorBrush = SolidColor(Color.Black),
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = "What would you like to accomplish?",
                            style = TextStyle(
                                fontSize = 22.sp,
                                color = Color(0xFFC5C9C5),
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}
