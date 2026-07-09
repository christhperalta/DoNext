package com.christhperalta.donext

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.christhperalta.donext.core.data.Settings
import com.christhperalta.donext.core.presentation.theme.DoNextTheme
import com.christhperalta.donext.di.appModule
import com.christhperalta.donext.domain.repository.CategoryRepository
import com.christhperalta.donext.domain.repository.TaskRepository
import com.christhperalta.donext.features.home.presentation.category_tasks.CategoryTasksScreen
import com.christhperalta.donext.features.home.presentation.create_todo.NewTaskScreen
import com.christhperalta.donext.features.home.presentation.main.MainScreen
import com.christhperalta.donext.features.onboarding.OnboardingScreen
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.dsl.koinConfiguration

sealed interface Screen : NavKey {
    @Serializable
    data object Onboarding : Screen

    @Serializable
    data object MainScreen : Screen

    @Serializable
    data class EditTask(val taskId: Long) : Screen

    @Serializable
    data object NewTask : Screen

    @Serializable
    data class CategoryTasks(val categoryName: String) : Screen

}

private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Screen.Onboarding::class, Screen.Onboarding.serializer())
            subclass(Screen.MainScreen::class, Screen.MainScreen.serializer())
            subclass(Screen.EditTask::class, Screen.EditTask.serializer())
            subclass(Screen.NewTask::class, Screen.NewTask.serializer())
            subclass(Screen.CategoryTasks::class, Screen.CategoryTasks.serializer())
        }
    }
}


@Composable
@Preview
fun App() {
    KoinApplication(
        configuration = koinConfiguration { modules(appModule) }
    ) {
        val settings: Settings = koinInject()
        val categoryRepository: CategoryRepository = koinInject()
        val taskRepository: TaskRepository = koinInject()
        val onboardingDone = remember { settings.getBoolean("onboarding_completed") }
        val initialScreen = if (onboardingDone) Screen.MainScreen else Screen.Onboarding
        val backStack = rememberNavBackStack(config, initialScreen)

        LaunchedEffect(Unit) {
            categoryRepository.seedDefaultCategories()
            taskRepository.permanentDeleteOldTasks()
        }

        DoNextTheme {
            NavDisplay(
                backStack = backStack,
                entryProvider = entryProvider {
                    entry<Screen.Onboarding> {
                        OnboardingScreen(
                            settings = settings,
                            onComplete = {
                                backStack.clear()
                                backStack.add(Screen.MainScreen)
                            }
                        )
                    }
                    entry<Screen.MainScreen> {
                        MainScreen(
                            onNavigateToNewTask = {
                                backStack.add(Screen.NewTask)
                            },
                            onNavigateToEditTask = { taskId ->
                                backStack.add(Screen.EditTask(taskId))
                            },
                            onNavigateToCategoryTasks = { categoryName ->
                                backStack.add(Screen.CategoryTasks(categoryName))
                            },
                        )
                    }
                    entry<Screen.EditTask>(
                        metadata = NavDisplay.transitionSpec {
                            slideInVertically(
                                initialOffsetY = { it },
                                animationSpec = tween(1000)
                            ) togetherWith ExitTransition.KeepUntilTransitionsFinished
                        } + NavDisplay.popTransitionSpec {
                            EnterTransition.None togetherWith
                                    slideOutVertically(
                                        targetOffsetY = { it },
                                        animationSpec = tween(1000)
                                    )
                        } + NavDisplay.predictivePopTransitionSpec {
                            EnterTransition.None togetherWith
                                    slideOutVertically(
                                        targetOffsetY = { it },
                                        animationSpec = tween(1000)
                                    )
                        }
                    ) {
                        val editTask = backStack.lastOrNull() as? Screen.EditTask
                        NewTaskScreen(
                            taskId = editTask?.taskId,
                            onBack = { backStack.removeLast() },
                        )
                    }
                    entry<Screen.CategoryTasks> {
                        val catTask = backStack.lastOrNull() as? Screen.CategoryTasks
                        CategoryTasksScreen(
                            categoryName = catTask?.categoryName ?: "",
                            onBack = { backStack.removeLast() },
                        )
                    }
                    entry<Screen.NewTask>(
                        metadata = NavDisplay.transitionSpec {
                            slideInVertically(
                                initialOffsetY = { it },
                                animationSpec = tween(1000)
                            ) togetherWith ExitTransition.KeepUntilTransitionsFinished
                        } + NavDisplay.popTransitionSpec {
                            EnterTransition.None togetherWith
                                    slideOutVertically(
                                        targetOffsetY = { it },
                                        animationSpec = tween(1000)
                                    )
                        } + NavDisplay.predictivePopTransitionSpec {
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