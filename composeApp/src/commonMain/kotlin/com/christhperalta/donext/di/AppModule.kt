package com.christhperalta.donext.di

import com.christhperalta.donext.core.data.TaskRepositoryImpl
import com.christhperalta.donext.domain.repository.TaskRepository
import com.christhperalta.donext.features.home.presentation.create_todo.NewTaskViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    single<TaskRepository> { TaskRepositoryImpl(get()) }
}

val domainModule = module {

}

val presentationModule = module {
    viewModel { NewTaskViewModel(get()) }
}

val appModule = listOf(
    platformModule,
    dataModule,
    domainModule,
    presentationModule
)