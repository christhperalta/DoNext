package com.christhperalta.donext.features.home.presentation.create_todo

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.christhperalta.donext.core.presentation.CustomFilledIconButton

@Composable
fun NewTaskScreen(
    onBack: () -> Unit
) {
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
                modifier = Modifier.align(Alignment.TopStart)
            )


            Column (
                modifier = Modifier.align(Alignment.Center),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {

            CategorySelector(
                label = "Category",
                dropDownLabel = "Personal",
                icon = Icons.AutoMirrored.Filled.List,
                iconDescription = "List icon",

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
                onClick = {}
            ){
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
                onClick = {onBack()}
            ){
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
) {

    val iconGreen = Color(0xFF81C784)
    val labelColor = Color(0xFF546E7A)

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

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
    }
}


@Composable
fun CustomInputScreen(
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .padding(16.dp)
    ) {
        BasicTextField(
            value = text,
            onValueChange = { text = it },
            // Estilo del texto que escribes
            textStyle = TextStyle(
                fontSize = 22.sp,
                color = Color(0xFF1A1C1A), // Verde oscuro casi negro
                fontWeight = FontWeight.Medium
            ),
            // Personalización del cursor
            cursorBrush = SolidColor(Color.Black),
            decorationBox = { innerTextField ->
                Box {
                    if (text.isEmpty()) {
                        Text(
                            text = "What would you like to accomplish?",
                            style = TextStyle(
                                fontSize = 22.sp,
                                color = Color(0xFFC5C9C5), // Gris suave para el placeholder
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                    innerTextField() // Aquí es donde se renderiza el texto real
                }
            }
        )
    }
}