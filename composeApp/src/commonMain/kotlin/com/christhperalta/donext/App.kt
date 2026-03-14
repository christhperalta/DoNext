package com.christhperalta.donext

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.christhperalta.donext.di.appModule
import com.christhperalta.donext.features.home.presentation.create_todo.NewTaskScreen
import com.christhperalta.donext.features.home.presentation.main.MainScreen
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.KoinApplication
import org.koin.dsl.koinConfiguration

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
            subclass(Screen.NewTask::class, Screen.NewTask.serializer())
        }
    }
}


@Composable
@Preview
fun App() {

    val backStack = rememberNavBackStack(config, Screen.MainScreen)


    KoinApplication(
        configuration = koinConfiguration { modules(appModule) }
    ) {
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
                    entry<Screen.NewTask>(
                        metadata = NavDisplay.transitionSpec {
                            // Slide new content up, keeping the old content in place underneath
                            slideInVertically(
                                initialOffsetY = { it },
                                animationSpec = tween(1000)
                            ) togetherWith ExitTransition.KeepUntilTransitionsFinished
                        } + NavDisplay.popTransitionSpec {
                            // Slide old content down, revealing the new content in place underneath
                            EnterTransition.None togetherWith
                                    slideOutVertically(
                                        targetOffsetY = { it },
                                        animationSpec = tween(1000)
                                    )
                        } + NavDisplay.predictivePopTransitionSpec {
                            // Slide old content down, revealing the new content in place underneath
                            EnterTransition.None togetherWith
                                    slideOutVertically(
                                        targetOffsetY = { it },
                                        animationSpec = tween(1000)
                                    )
                        }
                    ) {
                        NewTaskScreen(onBack = { backStack.removeLast() })
                    }
                }
            )
        }
    }

}