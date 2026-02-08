package com.christhperalta.donext.features.home.presentation.list


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
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
import com.christhperalta.donext.core.presentation.CustomFilledIconButton
import com.christhperalta.donext.core.presentation.CustomFloatingActionButton
import com.example.clickpos.core.ui.CustomText

@Composable
fun ListScreen( onNavigateToNewTask : ()-> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = { ListTopBar() },
        floatingActionButton = {
            CustomFloatingActionButton{onNavigateToNewTask()}
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(25.dp))
            CustomText(
                text = "Organize your life with ease.",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(20.dp))
            ListItem()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListTopBar() {
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
                icon = Icons.Default.Search,
                contentDescription = "Search",
                color = Color(0xFFF1F5EE)
            ){}

            CustomFilledIconButton(
                icon = Icons.Default.Add,
                contentDescription = "Add Task",
                color = Color(0xFFF1F5EE)
            ){}
        }
    )
}


@Composable
fun ListItem(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(40.dp)
    ) {

        Box(
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
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
                    text = "Personal",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                CustomText(text = "12 task", style = MaterialTheme.typography.labelLarge)
            }
        }


    }
}


