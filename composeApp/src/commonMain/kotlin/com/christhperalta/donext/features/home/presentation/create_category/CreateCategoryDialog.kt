package com.christhperalta.donext.features.home.presentation.create_category

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.christhperalta.donext.core.presentation.CategoryDefaults
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CreateCategoryDialog(
    onDismiss: () -> Unit,
    vm: CreateCategoryViewModel = koinViewModel()
) {
    val state by vm.state.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(
                text = "Create Category",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = state.name,
                onValueChange = { vm.onEvent(CreateCategoryEvents.OnNameChange(it)) },
                label = { Text("Category name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
            )

            Text(text = "Icon", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                CategoryDefaults.iconOptions.forEach { option ->
                    val isSelected = state.selectedIcon == option.key
                    val bgColor = if (isSelected) CategoryDefaults.parseColor(state.selectedColor) else Color(0xFFF1F5EE)
                    val iconColor = if (isSelected) Color.White else Color.Black

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(bgColor)
                            .then(
                                if (isSelected) Modifier.border(2.dp, bgColor, CircleShape)
                                else Modifier.border(1.dp, Color(0xFFE0E0E0), CircleShape)
                            )
                            .clickable { vm.onEvent(CreateCategoryEvents.OnIconChange(option.key)) },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = option.icon,
                            contentDescription = option.key,
                            tint = iconColor,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }
            }

            Text(text = "Color", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                CategoryDefaults.colorOptions.forEach { hex ->
                    val isSelected = state.selectedColor == hex
                    val color = CategoryDefaults.parseColor(hex)

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(color)
                            .then(
                                if (isSelected) Modifier.border(3.dp, Color.White, CircleShape)
                                else Modifier.border(1.dp, Color(0xFFE0E0E0), CircleShape)
                            )
                            .clickable { vm.onEvent(CreateCategoryEvents.OnColorChange(hex)) },
                        contentAlignment = Alignment.Center,
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp),
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    vm.onEvent(CreateCategoryEvents.OnCreateCategory)
                    onDismiss()
                },
                enabled = state.name.isNotBlank(),
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CategoryDefaults.parseColor(state.selectedColor)
                ),
            ) {
                Text(text = "Create", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }

            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Text(text = "Cancel", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }
        }
    }
}
