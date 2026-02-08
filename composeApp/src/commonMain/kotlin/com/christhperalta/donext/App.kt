package com.christhperalta.donext

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.christhperalta.donext.features.home.presentation.create_todo.NewTaskScreen
import com.christhperalta.donext.features.home.presentation.main.MainScreen

import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

sealed interface Screen : NavKey {
    @Serializable
   data object MainScreen : Screen

    @Serializable
   data object NewTask : Screen

}

private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Screen.MainScreen::class, Screen.MainScreen.serializer())
            subclass(Screen.NewTask::class,  Screen.NewTask.serializer())
        }
    }
}


@Composable
@Preview
fun App() {

    val backStack = rememberNavBackStack(config, Screen.MainScreen)

    MaterialTheme {
        NavDisplay(
            backStack = backStack,
            entryProvider = entryProvider {
                entry<Screen.MainScreen> {
                    MainScreen(

                        onNavigateToNewTask = {
                            backStack.add(Screen.NewTask)
                        })
                }
                entry<Screen.NewTask> {
                    NewTaskScreen(onBack = { backStack.removeLast() })
                }
            }
        )
    }
}