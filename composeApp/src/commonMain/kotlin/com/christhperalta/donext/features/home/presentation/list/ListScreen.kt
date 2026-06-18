package com.christhperalta.donext.features.home.presentation.list


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.christhperalta.donext.core.model.TaskCategory
import com.christhperalta.donext.core.presentation.CustomFilledIconButton
import com.christhperalta.donext.core.presentation.CustomFloatingActionButton
import com.christhperalta.donext.core.presentation.CustomText
import androidx.compose.foundation.lazy.grid.items

@Composable
fun ListScreen(onNavigateToNewTask: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = { ListTopBar(onNavigateToNewTask) },
        floatingActionButton = {
            CustomFloatingActionButton { onNavigateToNewTask() }
        },
    ) { innerPadding ->

        LazyVerticalGrid(
            modifier = Modifier.padding(innerPadding).padding(horizontal = 16.dp),
            columns = GridCells.Adaptive(minSize = 160.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
            item (
                span = { GridItemSpan(maxLineSpan) }
            ) {
                CustomText(
                    text = "Organize your life with ease.",
                    style = MaterialTheme.typography.titleMedium,

                )
            }
            items(TaskCategory.entries) { category ->
                ListItem(
                    title = category.name,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListTopBar(onNavigateToNewTask: () -> Unit) {
    TopAppBar(
        title = {
            CustomText(
                text = "Your List",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            CustomFilledIconButton(
                color = Color(0xFFF1F5EE),
                onClick = {}
            ) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }

            CustomFilledIconButton(
                color = Color(0xFFF1F5EE),
                onClick = { onNavigateToNewTask() }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    )
}


@Composable
fun ListItem(
    title: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(40.dp)
    ) {

        Box(
            modifier = Modifier
                .width(160.dp)
                .height(160.dp)
                .padding(25.dp)
        ) {
            Card(
                modifier = Modifier,
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(25.dp)
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Settings",
                    modifier = Modifier.padding(16.dp)
                )
            }




            Column(modifier = Modifier.align(alignment = Alignment.BottomStart)) {
                CustomText(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                CustomText(text = "12 task", style = MaterialTheme.typography.labelLarge)
            }
        }


    }
}


