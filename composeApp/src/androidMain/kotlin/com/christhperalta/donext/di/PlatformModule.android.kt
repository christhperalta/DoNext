package com.christhperalta.donext.di

import com.christhperalta.donext.core.data.DoNextDatabase
import com.christhperalta.donext.core.data.Settings
import com.christhperalta.donext.core.data.createDriver
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { DoNextDatabase(createDriver()) }
    single { Settings() }
}
