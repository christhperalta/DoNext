package com.christhperalta.donext.di

import com.christhperalta.donext.features.home.presentation.create_todo.NewTaskViewModel
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel
val dataModule = module {

}

val domainModule = module {

}

val presentationModule = module  {
    viewModel { NewTaskViewModel() }
}

val appModule = listOf(
    domainModule,
    dataModule,
    presentationModule

)