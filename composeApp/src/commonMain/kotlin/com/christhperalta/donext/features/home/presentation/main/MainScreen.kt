package com.christhperalta.donext.features.home.presentation.main



import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.christhperalta.donext.Screen
import com.christhperalta.donext.features.home.presentation.home.HomeScreen
import com.christhperalta.donext.features.home.presentation.list.ListScreen
import com.christhperalta.donext.features.home.presentation.stats.StatsScreen


enum class MainDestination(
    val route: Screen,
    val label: String,
    val icon: ImageVector
) {
    Today(Screen.Home, "Today", Icons.Default.CalendarToday),
    List(Screen.List, "List", Icons.Default.FilterList),
    Stats(Screen.Stats, "Stats", Icons.Default.Equalizer),
    Profile(Screen.Profile, "Profile", Icons.Default.Person)
}

val BrandGreen = Color(0xFF60DF20)
val BackgroundGray = Color(0xFFF5F7F5)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    backStack: NavBackStack<NavKey>
) {

    val currentKey = backStack.lastOrNull() ?: Screen.Home


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BackgroundGray,
        contentWindowInsets = WindowInsets.navigationBars,
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                MainDestination.entries.forEach { destination ->
                    NavigationBarItem(
                        selected = currentKey == destination.route,
                        onClick = {
                            backStack.clear()
                            backStack.add(destination.route)
                        },
                        label = { Text(destination.label) },
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = destination.label
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->

        NavDisplay(
            backStack = backStack,
            modifier = Modifier.padding(innerPadding),

            entryProvider = entryProvider {
                entry<Screen.Home> {
                    HomeScreen()
                }
                entry<Screen.List> {
                    ListScreen()
                }
                entry<Screen.Stats> {
                    StatsScreen()
                }
                entry<Screen.Profile> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = " Profile")
                    }
                }
            }
        )
    }
}

