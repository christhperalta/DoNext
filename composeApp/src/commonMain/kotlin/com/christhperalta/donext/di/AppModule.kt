package com.christhperalta.donext.di

import com.christhperalta.donext.core.data.Settings
import com.christhperalta.donext.core.data.CategoryRepositoryImpl
import com.christhperalta.donext.core.data.TaskRepositoryImpl
import com.christhperalta.donext.domain.repository.CategoryRepository
import com.christhperalta.donext.domain.repository.TaskRepository
import com.christhperalta.donext.features.home.presentation.category_tasks.CategoryTasksViewModel
import com.christhperalta.donext.features.home.presentation.create_category.CreateCategoryViewModel
import com.christhperalta.donext.features.home.presentation.create_todo.NewTaskViewModel
import com.christhperalta.donext.features.home.presentation.home.HomeViewModel
import com.christhperalta.donext.features.home.presentation.list.ListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    single<TaskRepository> { TaskRepositoryImpl(get()) }
    single<CategoryRepository> { CategoryRepositoryImpl(get()) }
}

val domainModule = module {

}

val presentationModule = module {
    viewModel { NewTaskViewModel(get(), get()) }
    viewModel { CreateCategoryViewModel(get()) }
    viewModel { ListViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { CategoryTasksViewModel(get()) }
}

val appModule = listOf(
    platformModule,
    dataModule,
    domainModule,
    presentationModule
)