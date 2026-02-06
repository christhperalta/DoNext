package com.christhperalta.donext

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.savedstate.serialization.SavedStateConfiguration
import com.christhperalta.donext.features.home.presentation.main.MainScreen

import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic


sealed interface Screen : NavKey {


   @Serializable object Home : Screen
    @Serializable object List : Screen
    @Serializable object Stats : Screen
    @Serializable object Profile : Screen
}

private  val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Screen.Home::class, Screen.Home.serializer())
            subclass(Screen.List::class, Screen.List.serializer())
            subclass(Screen.Stats::class, Screen.Stats.serializer())
            subclass(Screen.Profile::class, Screen.Profile.serializer())

        }
    }
}



@Composable
@Preview
fun App() {

    val backStack = rememberNavBackStack(config , Screen.Home)

    MaterialTheme {
        MainScreen(backStack)
    }
}