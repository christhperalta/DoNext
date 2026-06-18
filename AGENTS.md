# DoNext — AGENTS.md

## Project

Kotlin Multiplatform + Compose Multiplatform todo app targeting **Android** and **iOS**.

## Modules & Entrypoints

| Module | Kind | Package | Entrypoint |
|---|---|---|---|
| `:composeApp` | KMP shared library | `com.christhperalta.donextlibrary` | `composeApp/src/commonMain/…/App.kt` (shared `@Composable`) |
| | | | iOS: `composeApp/src/iosMain/…/MainViewController.kt` |
| `:androidApp` | Android app | `com.christhperalta.donext` | `androidApp/src/main/…/MainActivity.kt` → calls `App()` |

## Build

```sh
# Android debug APK
./gradlew :composeApp:assembleDebug

# Run Android (device/emulator)
./gradlew :androidApp:installDebug

# Run all checks (test only — no lint/typecheck configured)
./gradlew :composeApp:check
```

- AGP 9.2.1, Kotlin 2.4.0, Compose Multiplatform 1.11.1, JVM target 11
- Version catalog at `gradle/libs.versions.toml`
- Gradle config cache and build cache enabled

## Architecture

- **Single-activity** Android; iOS uses `ComposeUIViewController`
- **Navigation**: JetBrains Navigation3 (`org.jetbrains.androidx.navigation3`)
- **DI**: Koin (`koin-compose` 4.2.0-RC1, viewmodel navigation support)
- **Charts**: Vico 3.2.2 (`compose` + `compose-m3`)
- **Icons**: Material Icons Extended
- **Feature packaging**: `features/home/presentation/{home,list,stats,profile,create_todo,main}`
- **State pattern**: sealed `Events` + data `State` + `ViewModel` (Koin-injected via `viewModel { }`)

## Known Bugs (fix before making changes)

1. `CustomText.kt`, `CustomButton.kt`, `CustomTextField.kt` — declare `package com.example.clickpos.core.ui` instead of `com.christhperalta.donext.core.presentation`
2. `StatsScreen.kt:43` and `ListScreen.kt:36` — import `com.example.clickpos.core.ui.CustomText` instead of `com.christhperalta.donext.core.presentation.CustomText`
3. `NewTaskViewModel.kt:10-11` — references `NewTackState` (typo, should be `NewTaskState`)

## Testing

- Placeholder test at `composeApp/src/commonTest/…/ComposeAppCommonTest.kt`
- No UI or integration tests yet

## Conventions

- Use `state: StateFlow<…>` + `onEvent(…)` pattern for ViewModels
- Navigation3 polymorphic serialization requires registering all `NavKey` subclasses in `SavedStateConfiguration.serializersModule`
- All shared UI lives in `commonMain`; platform-specific code only where unavoidable (iOS entrypoint)
- `BrandGreen = Color(0xFF60DF20)` and `BackgroundGray = Color(0xFFF5F7F5)` defined in `MainScreen.kt`
