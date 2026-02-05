package com.christhperalta.donext.features.home.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.christhperalta.donext.features.home.presentation.main.BrandGreen
import donext.composeapp.generated.resources.Res
import donext.composeapp.generated.resources.profile_img
import org.jetbrains.compose.resources.painterResource


data class FilterOption(
    val id: Int,
    val text: String,
    val isActive: Boolean = false
)
@Composable
fun HomeScreen(
) {

    val filterOptions = remember {
        listOf(
            FilterOption(1, "All Tasks", true),
            FilterOption(2, "Priority"),
            FilterOption(3, "Focus"),
            FilterOption(4, "Personal")
        )
    }




    Scaffold(
        modifier = Modifier.fillMaxSize(),

        topBar = {
            HomeTopBar(userName = "Christh")
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO */ },
                containerColor = BrandGreen,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        },

    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { HeaderSection(userName = "Christh", taskCount = 4) }

            item { FilterRow(filterOptions) }

            item {
                TaskCard(
                    title = "Morning Meditation",
                    subtitle = "10 minutes focus session"
                )
            }
        }
    }
}


@Composable
private fun HeaderSection(userName: String, taskCount: Int) {
    Column(modifier = Modifier.padding(top = 16.dp)) {
        Text(
            text = "Hello, $userName.",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "You have $taskCount tasks today.",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = BrandGreen
        )
    }
}

@Composable
private fun FilterRow(options: List<FilterOption>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(options, key = { it.id }) { option ->
            val containerColor = if (option.isActive) BrandGreen else Color.White
            val contentColor = if (option.isActive) Color.White else Color.Black

            Button(
                onClick = { /* TODO: Update State */ },
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
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent // El Scaffold ya tiene el fondo
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
        title = { Text(text = userName, fontWeight = FontWeight.Bold) },
        actions = {
            IconButton(onClick = {}) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
        }
    )
}

@Composable
private fun TaskCard(title: String, subtitle: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Check Circle
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .border(1.dp, Color.LightGray, CircleShape)
            )

            Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold)
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            IconButton(onClick = {}) {
                Icon(Icons.Default.ArrowRight, contentDescription = "Open Task")
            }
        }
    }
}
