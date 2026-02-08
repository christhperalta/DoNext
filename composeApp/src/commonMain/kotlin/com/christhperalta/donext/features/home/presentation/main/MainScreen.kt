package com.christhperalta.donext.features.home.presentation.main


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.christhperalta.donext.features.home.presentation.home.HomeScreen
import com.christhperalta.donext.features.home.presentation.list.ListScreen
import com.christhperalta.donext.features.home.presentation.stats.StatsScreen
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic


sealed interface TabScreen : NavKey {
    @Serializable
    data object Home : TabScreen

    @Serializable
    data object List : TabScreen

    @Serializable
    data object Stats : TabScreen

    @Serializable
    data object Profile : TabScreen

}


private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(TabScreen.Home::class, TabScreen.Home.serializer())
            subclass(TabScreen.List::class, TabScreen.List.serializer())
            subclass(TabScreen.Stats::class, TabScreen.Stats.serializer())
            subclass(TabScreen.Profile::class, TabScreen.Profile.serializer())
        }
    }
}

enum class MainDestination(
    val route: TabScreen,
    val label: String,
    val icon: ImageVector
) {
    Today(TabScreen.Home, "Today", Icons.Default.CalendarToday),
    List(TabScreen.List, "List", Icons.Default.FilterList),
    Stats(TabScreen.Stats, "Stats", Icons.Default.Equalizer),
    Profile(TabScreen.Profile, "Profile", Icons.Default.Person)
}

val BrandGreen = Color(0xFF60DF20)
val BackgroundGray = Color(0xFFF5F7F5)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToNewTask: () -> Unit
) {

    val tabBackStack = rememberNavBackStack(config, TabScreen.Home)
    val currentKey = tabBackStack.lastOrNull() ?: TabScreen.Home


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
//                            if (tabBackStack.isNotEmpty()) {
//                                tabBackStack[0] = destination.route
//                            } else {
//                                tabBackStack.add(destination.route)
//                            }
                            if (currentKey != destination.route) {
                                tabBackStack.clear()
                                tabBackStack.add(destination.route)
                            }
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
            backStack = tabBackStack,
            modifier = Modifier.padding(innerPadding),
            onBack = {
                // Si hay más de una pantalla (ej: [Home, Profile]), quita la de arriba
                if (tabBackStack.size > 1) {
                    tabBackStack.removeLast()
                }
            },
            entryProvider = entryProvider {
                entry<TabScreen.Home> {
                    HomeScreen(
                        onNavigateToNewTask = onNavigateToNewTask,
                        onNavigateToProfile = { tabBackStack.add(TabScreen.Profile) })
                }
                entry<TabScreen.List> {
                    ListScreen( onNavigateToNewTask = onNavigateToNewTask,)
                }
                entry<TabScreen.Stats> {
                    StatsScreen()
                }
                entry<TabScreen.Profile> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = " Profile")
                    }
                }

            }
        )
    }
}
